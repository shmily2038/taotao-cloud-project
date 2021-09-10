package com.taotao.cloud.health.warn;

import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.model.DingerRobot;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;

public class DingdingWarn extends AbstractWarn {

	private DingerRobot dingerRobot;
	private WarnProperties warnProperties;

	public DingdingWarn(WarnProperties warnProperties, DingerRobot dingerRobot) {
		this.warnProperties = warnProperties;
		this.dingerRobot = dingerRobot;
	}

	@Override
	public void notify(Message message) {
		if (this.dingerRobot != null) {
			String ip = RequestUtil.getIpAddress();
			String dingDingFilterIP = this.warnProperties.getDingdingFilterIP();
			if (!StringUtil.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {

				String title =
					"[" + message.getWarnType().getDescription() + "]" + StringUtil.subString3(
						message.getTitle(), 100);

				String context = StringUtil.subString3(message.getTitle(), 100) + "\n" +
					"详情: " + RequestUtil.getBaseUrl() + "/taotao/cloud/health/\n" +
					StringUtil.subString3(message.getContent(), 500);

				MessageSubType messageSubType = MessageSubType.TEXT;
				DingerRequest request = DingerRequest.request(context, title);

				dingerRobot.send(messageSubType, request);
			}
		}
	}
}
