/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

/**
 * Simple email delivery thread.
 * 
 * @author mbooth
 */
public class EmailThread implements Runnable {
  private volatile Thread thread;

  /**
   * Start the thread. This method is a no-op if the thread is already running.
   */
  void start() {
    if (thread == null) {
      thread = new Thread(new EmailThread(), "Email-Thread");
      thread.setDaemon(true);
      thread.start();
    }
  }

  /**
   * Stop the thread. Waits until the thread exits before returning. This method is a
   * no-op if the thread was not running.
   */
  void stop() throws InterruptedException {
    if (thread != null) {
      Thread t = thread;
      thread = null;
      t.interrupt();
      t.join();
    }
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        // TODO do stuff
        Thread.sleep(30000);
      } catch (InterruptedException e) {
        // Allow thread to exit if interrupted by stop() method
        if (thread == null) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}
