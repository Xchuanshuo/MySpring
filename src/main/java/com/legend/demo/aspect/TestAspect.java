package com.legend.demo.aspect;

import com.legend.annotation.Aspect;
import com.legend.annotation.PointCut;
import com.legend.factory.AbsMethodAdvance;

/**
 * @author Legend
 * @data by on 18-10-28.
 * @description
 */
@Aspect
public class TestAspect extends AbsMethodAdvance {

    @PointCut("com.legend.demo.aspect.Test_doSomething")
    public void testAspect() {

    }

    @Override
    public void doBefore() {
        System.out.println("do before....");
    }

    @Override
    public void doAfter() {
        System.out.println("do after....");
    }
}
