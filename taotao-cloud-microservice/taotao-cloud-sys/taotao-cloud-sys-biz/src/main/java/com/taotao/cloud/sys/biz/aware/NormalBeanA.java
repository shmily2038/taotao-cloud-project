package com.taotao.cloud.sys.biz.aware;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 可以看到，这个类也是Aware扩展的一种，触发点在bean的初始化之前，也就是postProcessBeforeInitialization之前，这个类的触发点方法只有一个：setBeanName
 *
 * 使用场景为：用户可以扩展这个点，在初始化bean之前拿到spring容器中注册的的beanName，来自行修改这个beanName的值。
 */
public class NormalBeanA implements BeanNameAware , InitializingBean, DisposableBean {
    public NormalBeanA() {
        System.out.println("NormalBean constructor");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("[BeanNameAware] " + name);
    }

	/**
	 * 这个类，顾名思义，也是用来初始化bean的。InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候都会执行该方法。这个扩展点的触发时机在postProcessAfterInitialization之前。
	 *
	 * 使用场景：用户实现此接口，来进行系统启动的时候一些业务指标的初始化工作。
	 *
	 * 扩展方式为：
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("[InitializingBean] NormalBeanA");
	}

	/**
	 * 这个扩展点也只有一个方法：destroy()，其触发时机为当此对象销毁时，会自动执行这个方法。比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
	 * @throws Exception
	 */
	@Override
	public void destroy() throws Exception {
		System.out.println("[DisposableBean] NormalBeanA");
	}

	/**
	 * 这个并不算一个扩展点，其实就是一个标注。其作用是在bean的初始化阶段，如果对一个方法标注了@PostConstruct，会先调用这个方法。这里重点是要关注下这个标准的触发点，这个触发点是在postProcessBeforeInitialization之后，InitializingBean.afterPropertiesSet之前。
	 *
	 * 使用场景：用户可以对某一方法进行标注，来进行初始化某一个属性
	 */
	@PostConstruct
	public void init(){
		System.out.println("[PostConstruct] NormalBeanA");
	}
}
