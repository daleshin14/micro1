package ru.diasoft.micro.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.yaml.snakeyaml.Yaml;
import ru.diasoft.micro.gateway.cache.Cache;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
public class RuleListFilter implements GatewayFilterFactory<RuleListFilter.Config> {

    Logger logger = LoggerFactory.getLogger(RuleListFilter.class);

    @Value("${gateway.params.rule-list-dir}")
    String whiteListDir;

    @Value("${gateway.params.rule-list-name}")
    String whiteListName;

    @Value("${gateway.params.rule-list-cache-time-live-min}")
    String cacheTimeLive;

    Supplier<RuleList> rule;

    @PostConstruct
    void init() {
        rule = Cache.cache(() -> {
            logger.info("Update cached rule list");
            Yaml yaml = new Yaml();
            File yamlFile = new File(whiteListDir, whiteListName);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(yamlFile);
            } catch (FileNotFoundException e) {
                logger.error(String.format("File not found: '%s'", yamlFile.getAbsolutePath()));
                return new RuleList(new ArrayList<>(), new ArrayList<>());
            }
            Map whiteList = yaml.load(inputStream);
            List<String> blacklist = new ArrayList<>(Arrays.asList(whiteList.getOrDefault("blacklist", "").toString().split("\\n")));
            List<String> whitelist = new ArrayList<>(Arrays.asList(whiteList.getOrDefault("whitelist", "").toString().split("\\n")));
            return new RuleList(blacklist, whitelist);
        }, Long.parseLong(cacheTimeLive));
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String url = exchange.getRequest().getPath().value();
            whiteListName = whiteListName.endsWith(".yaml") ? whiteListName : whiteListName + ".yaml";

            Boolean isAllowed = blacklistFilter.negate().and(whitelistFilter).test(exchange);
            if (!isAllowed) {
                logger.info(String.format("Url '%s' is not allowed", url));
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            logger.info(String.format("Url '%s' is allowed", url));
            return chain.filter(exchange);
        };
    }

    private Boolean checkMethod(String method, String pattern) {
        String allowMethods[] = pattern.replaceFirst("/.*", "").split(",");
        if (allowMethods[0].length() == 0)
            return true;
        Long isAllow = Arrays.stream(allowMethods)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> s.equals(method.toLowerCase()))
                .count();
        return isAllow > 0;
    }

    private Boolean checkUrl(String url, String pattern) {
        pattern = pattern.substring(pattern.indexOf("/"));
        try {
            PathPatternParser parser = new PathPatternParser();
            parser.setMatchOptionalTrailingSeparator(true);
            PathPattern p = parser.parse(pattern);
            PathContainer pc = PathContainer.parsePath(url);
            return p.matches(pc);
        } catch (Exception e) {
            logger.error(String.format("Error parsing template '%s' in white list", pattern));
            return false;
        }
    }

    private Boolean check(ServerWebExchange exchange, String pattern) {
        String url = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethodValue();
        return checkUrl(url, pattern) && checkMethod(method, pattern);
    }

    Predicate<ServerWebExchange> blacklistFilter = (exchange) -> {
        RuleList ruleList = rule.get();
        return ruleList.blacklist.stream()
                .map(pattern -> check(exchange, pattern))
                .reduce((ok, cur) -> ok |= cur)
                .orElse(false);
    };

    Predicate<ServerWebExchange> whitelistFilter = (exchange) -> {
        RuleList ruleList = rule.get();
        return ruleList.whitelist.stream()
                .map(pattern -> check(exchange, pattern))
                .reduce((ok, cur) -> ok |= cur)
                .orElse(false);
    };

    @Override
    public Config newConfig() {
        return new Config("RuleListFilter");
    }

    static class Config {
        Config(String name){
            this.name = name;
        }
        private String name;

        String getName() {
            return name;
        }
    }

}
