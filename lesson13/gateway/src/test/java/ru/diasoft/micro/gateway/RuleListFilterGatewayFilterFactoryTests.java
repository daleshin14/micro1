package ru.diasoft.micro.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import ru.diasoft.micro.gateway.filters.RuleListFilter;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RuleListFilterGatewayFilterFactoryTests {

	@Autowired
	private RuleListFilter ruleListFilter;
	
	@Test
	public void checkRuleList() {
		testRuleListFilter(HttpMethod.GET, "/corews/block2", HttpStatus.OK);
		testRuleListFilter(HttpMethod.PUT, "/corews/block2", HttpStatus.FORBIDDEN);
		testRuleListFilter(HttpMethod.POST, "/corews/block2", HttpStatus.FORBIDDEN);
		testRuleListFilter(HttpMethod.DELETE, "/corews/block2", HttpStatus.OK);
		testRuleListFilter(HttpMethod.GET, "/corews/api/v1/ping", HttpStatus.OK);
		testRuleListFilter(HttpMethod.PUT, "/corews/api/v1/ping", HttpStatus.FORBIDDEN);
		testRuleListFilter(HttpMethod.POST, "/corews/api/v1/ping", HttpStatus.FORBIDDEN);
		testRuleListFilter(HttpMethod.DELETE, "/corews/api/v1/ping", HttpStatus.OK);
		testRuleListFilter(HttpMethod.GET, "/template/api/v2/get-template", HttpStatus.OK);
		testRuleListFilter(HttpMethod.PUT, "/template/api/v2/get-template", HttpStatus.OK);
		testRuleListFilter(HttpMethod.POST, "/template/api/v2/get-template", HttpStatus.OK);
		testRuleListFilter(HttpMethod.DELETE, "/template/api/v2/get-template", HttpStatus.OK);
		testRuleListFilter(HttpMethod.GET, "/simple-rest-service/api/v1/find", HttpStatus.OK);
		testRuleListFilter(HttpMethod.PUT, "/simple-rest-service/api/v1/find", HttpStatus.OK);
		testRuleListFilter(HttpMethod.POST, "/simple-rest-service/api/v1/find", HttpStatus.OK);
		testRuleListFilter(HttpMethod.DELETE, "/simple-rest-service/api/v1/find", HttpStatus.OK);
	}
	
	private void testRuleListFilter(HttpMethod method, String path, HttpStatus expectedStatus) {
		GatewayFilter filter = ruleListFilter.apply(ruleListFilter.newConfig());

		MockServerHttpRequest request = MockServerHttpRequest.method(method, "http://localhost" + path).build();
		ServerWebExchange exchange = MockServerWebExchange.from(request);
		GatewayFilterChain filterChain = mock(GatewayFilterChain.class);

		when(filterChain.filter(exchange)).thenAnswer(new Answer<Mono<Void>>() {
			public Mono<Void> answer(InvocationOnMock invocation) throws Throwable {
				ServerWebExchange exchange = (ServerWebExchange) invocation.getArguments()[0];
				exchange.getResponse().setStatusCode(expectedStatus);
				return exchange.getResponse().setComplete();
			}
		});
		
		filter.filter(exchange, filterChain);

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(expectedStatus);
	}
}
