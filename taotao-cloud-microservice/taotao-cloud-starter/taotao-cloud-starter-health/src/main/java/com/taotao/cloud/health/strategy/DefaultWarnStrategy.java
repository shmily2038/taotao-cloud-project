package com.taotao.cloud.health.strategy;

import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.health.collect.IOCollectTask;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.strategy.Rule.RulesAnalyzer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认报警策略
 *
 * @author: chejiangyi
 * @version: 2019-07-28 08:24
 **/
public class DefaultWarnStrategy {

	protected static int maxCacheSize = 3;
	protected static List<Report> cacheReports = Collections.synchronizedList(
		new ArrayList<>(maxCacheSize + 2));
	protected Rule.RulesAnalyzer rulesAnalyzer;
	protected WarnTemplate warnTemplate;

	public Rule.RulesAnalyzer getRulesAnalyzer() {
		return rulesAnalyzer;
	}

	public WarnTemplate getWarnTemplate() {
		return warnTemplate;
	}

	public DefaultWarnStrategy(WarnTemplate warnTemplate, Rule.RulesAnalyzer rulesAnalyzer) {
		this.warnTemplate = warnTemplate;
		this.rulesAnalyzer = rulesAnalyzer;

		this.setDefaultStrategy();
	}

	public void setDefaultStrategy() {
		this.rulesAnalyzer.registerRules("cpu.process",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.cpu.process", "[>0.7]"));
		this.rulesAnalyzer.registerRules("cpu.system",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.cpu.system", "[>0.7]"));
		this.rulesAnalyzer.registerRules("io.current.dir.usable.size",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.io.current.dir.usable.size",
				"[<500]"));
		this.rulesAnalyzer.registerRules("memery.jvm.max",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.memery.jvm.max", "[<256]"));
		this.rulesAnalyzer.registerRules("memery.system.free",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.memery.system.free",
				"[<256]"));
		this.rulesAnalyzer.registerRules("thread.deadlocked.count",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.thread.deadlocked.count",
				"[>10]"));
		this.rulesAnalyzer.registerRules("thread.total",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.thread.total", "[>1000]"));
		this.rulesAnalyzer.registerRules("tomcat.threadPool.poolSize.count",
			PropertyUtil.getPropertyCache(
				"taotao.cloud.health.strategy.tomcat.threadPool.poolSize.count",
				"[>1000]"));
		this.rulesAnalyzer.registerRules("tomcat.threadPool.active.count",
			PropertyUtil.getPropertyCache(
				"taotao.cloud.health.strategy.tomcat.threadPool.active.count",
				"[>200]"));
		this.rulesAnalyzer.registerRules("tomcat.threadPool.queue.size",
			PropertyUtil.getPropertyCache(
				"taotao.cloud.health.strategy.tomcat.threadPool.queue.size",
				"[>50]"));
		if (this.rulesAnalyzer.getRules("io.current.dir.usable.size") != null) {
			//设置报警回调
			this.rulesAnalyzer.getRules("io.current.dir.usable.size").forEach(c -> {
				if (c.getType() == Rule.RuleType.less) {
					c.setHitCallBack((value) -> IOCollectTask.clearlog());
				}
			});
		}
	}

	public Report analyse(Report report) {
		while (cacheReports.size() > maxCacheSize) {
			cacheReports.remove(0);
		}
		cacheReports.add(report);
		Report avgReport = report.avgReport(cacheReports);
		return this.rulesAnalyzer.analyse(avgReport);
	}

	public String analyseText(Report report) {
		Report r = analyse(report);
		StringBuilder warn = new StringBuilder();
		r.eachReport((filed, item) -> {
			if (item.isWarn()) {
				warn.append(warnTemplate.getWarnConent(filed, item.getDesc(), item.getValue(),
					item.getRule()));
			}
			return item;
		});
		String warninfo = warn.toString();
		if (warninfo == null || warninfo.isEmpty()) {
			return "";
		}
		return warninfo;
	}

}
