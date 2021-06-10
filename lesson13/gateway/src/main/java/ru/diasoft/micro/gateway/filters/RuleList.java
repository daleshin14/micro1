package ru.diasoft.micro.gateway.filters;

import java.util.ArrayList;
import java.util.List;

public class RuleList {
    List<String> blacklist = new ArrayList<>();
    List<String> whitelist = new ArrayList<>();

    public RuleList(List<String> blacklist, List<String> whitelist) {
        this.blacklist = blacklist;
        this.whitelist = whitelist;
    }
}
