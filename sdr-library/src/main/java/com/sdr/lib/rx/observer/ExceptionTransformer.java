package com.sdr.lib.rx.observer;

/**
 * Created by HyFun on 2019/11/8.
 * Email: 775183940@qq.com
 * Description:
 */
public class ExceptionTransformer {
    private Class exceptionClass;
    private ExceptionSolver solver;

    public ExceptionTransformer(Class exceptionClass, ExceptionSolver solver) {
        this.exceptionClass = exceptionClass;
        this.solver = solver;
    }

    public Class getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(Class exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public ExceptionSolver getSolver() {
        return solver;
    }

    public void setSolver(ExceptionSolver solver) {
        this.solver = solver;
    }
}
