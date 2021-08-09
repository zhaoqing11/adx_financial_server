//package com.curtain.project.controller;
//
//import InspectPlan;
//import InspectPlanServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class test implements Runnable {
//
//    private InspectPlan inspectPlan;
//
//    @Autowired
//    private InspectPlanServiceImpl inspect = new InspectPlanServiceImpl();
//
//    private test(InspectPlan inspectPlan) {
//        this.inspectPlan = inspectPlan;
//    }
//
//    @Override
//    public void run() {
//        inspect.insertSelective(inspectPlan);
//    }
//
//    public static void main(String[] args) {
//        InspectPlan inspectPlan = new InspectPlan();
//        inspectPlan.setIdInspectPlan(1);
//
//        InspectPlan inspectPlan1 = new InspectPlan();
//        inspectPlan1.setIdInspectPlan(2);
//
//        Thread t1 = new Thread(new test(inspectPlan));
//        Thread t2 = new Thread(new test(inspectPlan1));
//
//        t1.start();
//        t2.start();
//    }
//}
