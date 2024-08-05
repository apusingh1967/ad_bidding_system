package com.adbiddingsystem.domain;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;

public class Junk {
    public static void main(String[] args) throws Exception {
        var exports = 1;
        ReferenceQueue<Jjj>q = new ReferenceQueue<>();
        var j = new Jjj();
        var o = new MyPR(j, q);
        j = null;
        System.gc();
        for(int i = 0; i < 1000000; i++) new Jjj();
        Thread.sleep(1000);
        System.out.println(o.isEnqueued());
        System.gc();

        Reference<? extends Jjj> r= null;
        while((r = q.poll()) != null){
            System.out.println(r.get() + ", " + r.isEnqueued());
            r.clear();
        }
    }

    static class MyPR extends PhantomReference<Jjj>{

      //  private final Jjj jjj;

        public MyPR(Jjj referent, ReferenceQueue<? super Jjj> q) {
            super(referent, q);
         //   this.jjj = referent;
        }

        public void clean(){
          //  jjj.clean();
        }

    }

    static class Jjj{
        @Override
        protected void finalize() throws Throwable {
           // System.out.println("finalized...");
            super.finalize();
        }

        public void clean(){
            System.out.println("clean...");
        }

        @Override
        public String toString() {
            return "muy jjj";
        }
    }

}
