package com.mymodule.adbiddingsystem.domain;

import com.mymodule.adbiddingsystem.common.messages.Message;

/*
based on actor model like Akka or Erlang kind of concurrent objects interacting in real time
and optimizing cache locality to ensure each thread is pinned to a core to ensure max performance (hopefully).
If there are no extraneous reasons, it is possible that actor runs on same core for as long as possible
 */
public abstract class Actor implements Runnable {

    public abstract void receive(Message message);

}
