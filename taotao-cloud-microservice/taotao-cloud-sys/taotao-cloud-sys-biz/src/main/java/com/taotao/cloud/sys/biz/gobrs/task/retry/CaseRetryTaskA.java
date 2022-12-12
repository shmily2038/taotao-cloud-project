package com.taotao.cloud.sys.biz.gobrs.task.retry;

import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: gobrs-async
 * @ClassName GobrsTaskA
 * @description:
 * @author: sizegang
 * @create: 2022-10-31
 **/
@Slf4j
@Task
public class CaseRetryTaskA extends AsyncTask {

	@Override
	public void prepare(Object o) {
		log.info(this.getName() + " 使用线程---" + Thread.currentThread().getName());
	}

	@Override
	public Object task(Object o, TaskSupport support) {
		System.out.println("CaseRetryTaskA Begin");
		System.out.println("CaseRetryTaskA End");
		return "AResult";
	}
}
