/**
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crackj2ee.midp.gtalk.component;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.crackj2ee.midp.gtalk.MainCanvas;

/**
 * List all recent messages.
 * 
 * @author xuefeng
 */
public class MessageList extends ComponentUI {

    private int maxWidth;
    private int MAX_MESSAGES = 100;
    private int lineHeight;
    private int visibleLines;
    private Vector messages = new Vector();
    private int start = (-1);
    private String lastEmail = null;
    private Scrollable scrollable;

    public MessageList(int x, int y, int width, int height, Font font, Scrollable scrollable) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        maxWidth = width - 4;
        lineHeight = font.getHeight() + 2;
        visibleLines = (height - 2) / lineHeight;
    }

    public synchronized void addReceivedMessage(String name, String email, String text) {
        Vector v = splitMessage(text);
        synchronized(this) {
            if(!email.equals(lastEmail)) {
                if(lastEmail!=null)
                    messages.addElement("");
                Vector lines = splitLongString(name + ":");
                addAll(messages, lines);
                lastEmail = email;
            }
            addAll(messages, v);
            removeOld();
            if(start==(-1))
                start = 0;
        }
    }

    public synchronized void addSentMessage(String name, String text) {
        Vector v = splitMessage(text);
        String t = "To " + name + ":";
        synchronized(this) {
            if(!t.equals(lastEmail)) {
                if(lastEmail!=null)
                    messages.addElement("");
                Vector lines = splitLongString(t);
                addAll(messages, lines);
                lastEmail = t;
            }
            addAll(messages, v);
            removeOld();
            if(start==(-1))
                start = 0;
        }
    }

    private void removeOld() {
        if(messages.size()>MAX_MESSAGES) {
            int n = messages.size() - MAX_MESSAGES;
            for(int i=0; i<n; i++)
                messages.removeElementAt(0);
        }
    }

    public synchronized boolean moveUp() {
        if(start>0) {
            start--;
            return true;
        }
        return false;
    }

    public synchronized boolean moveDown() {
        if(start<messages.size()-1) {
            start++;
            return true;
        }
        return false;
    }

    private int getScrollMax() {
        int n = messages.size() - visibleLines + 1;
        return (n<0 ? 0 : n);
    }

    public boolean shouldScrollDown() {
        if(getScrollMax()==0)
            return true;
        return getScrollMax()==(start+1);
    }

    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(getScrollMax(), start);
        scrollable.paint(g);
    }

    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        //g.setColor(MainCanvas.COLOR_BORDER);
        //g.drawRect(0, 0, width-1, height-1);
        // draw each message:
        if(messages.size()==0)
            return;
        g.setColor(MainCanvas.COLOR_FOREGROUND);
        if(start==(-1))
            start = 0;

        synchronized(this) {
            int end = start + visibleLines;
            if(end>messages.size()) {
                end = messages.size();
                start = end - visibleLines;
                if(start<0)
                    start = 0;
            }
            for(int i=start; i<end; i++) {
                g.drawString((String)messages.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
            }
        }
    }

    private void addAll(Vector dest, Vector src) {
        for(int i=0; i<src.size(); i++)
            dest.addElement(src.elementAt(i));
    }

    private Vector splitMessage(String text) {
        // split to lines by '\n':
        int start = 0;
        text = text.replace('\r', '\n');
        Vector lines = new Vector();
        for(;;) {
            int n = text.indexOf('\n', start);
            if(n==(-1)) {
                // last line:
                String s = text.substring(start).trim();
                if(!s.equals("")) {
                    System.out.println("LINE>>" + s);
                    lines.addElement(text.substring(start));
                }
                break;
            }
            if(n==0) {
                // empty line!
                start++;
                continue;
            }
            String s = text.substring(start, n).trim();
            if(!s.equals("")) {
                System.out.println("LINE>>" + s);
                lines.addElement(s);
            }
            start = n + 1;
        }
        // now append each lines:
        Vector moreLines = new Vector();
        for(int i=0; i<lines.size(); i++) {
            Vector v = splitLongString((String)lines.elementAt(i));
            addAll(moreLines, v);
        }
        return moreLines;
    }

    private Vector splitLongString(String text) {
        Vector v = new Vector();
        split(text, v);
        return v;
    }

    private void split(String text, Vector toAdd) {
        System.out.println("-- split: " + text);
        String leftPart = left(text, maxWidth);
        if(leftPart==null) {
            System.out.println(">>" + text);
            System.out.println("Done!");
            toAdd.addElement(text);
            return;
        }
        // add left:
        System.out.println(">>" + leftPart);
        toAdd.addElement(leftPart);
        // call recursively:
        String rightPart = text.substring(leftPart.length()).trim();
        if(!rightPart.equals(""))
            split(rightPart, toAdd);
    }

}
