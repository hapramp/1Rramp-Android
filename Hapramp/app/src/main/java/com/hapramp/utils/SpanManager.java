package com.hapramp.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ankit on 1/27/2018.
 */

public class SpanManager {

    private static final int SPAN_TYPE_SIZE = 1;
    private String spannedContent;
    private StringBuilder originalContent;
    private int currentSegmentIndex;
    private ArrayList<Segment> segments;
    private int contentLen;

    public SpanManager() {
        originalContent = new StringBuilder();
        segments = new ArrayList<>();
        //init the segment with default 1 Segment
        createNewSegment(0);

    }

    public void setSpan(int type, int cursorIndex) {
        //search for segments
        int segInd = getSegmentIndex(cursorIndex);
        if (segInd == -1) {
            // no existing segment exists
            //create new one
            createNewSegment(cursorIndex);
            currentSegmentIndex = 0;
            segments.get(0).addSpan(type);

        } else {
            // set the span
            segments.get(segInd).addSpan(type);
            currentSegmentIndex = segInd;
        }

    }

    public void removeSpan(int type, int cursorIndex) {
        // it is called when already having a span
        int segId = getSegmentIndex(cursorIndex);
        segments.get(segId).removeSpan(type);

    }

    private int getSegmentIndex(int cursorIndex) {

        int i = 0;
        for (Segment segment : segments) {
            if (segment.startIndex <= cursorIndex && segment.endIndex > cursorIndex) {
                l("Cursor "+cursorIndex+" is in "+i+" th Segment");
                return i;
            }
            i++;
        }
        return -1;
    }

    // called when: a cursor shift is observed
    public void refreshCursorIndex(int index) {

        int i = 0;
        for (Segment segment : segments) {
            if (segment.startIndex <= index && segment.endIndex >= index) {
                currentSegmentIndex = i;
            }
            i++;
        }

    }

    public void createNewSegment(int index) {

        l("Creating new segment");
        Segment segment = new Segment(index);
        segment.startIndex = index;
        segment.endIndex = index;
        segments.add(segment);

    }

    public void addCharacter(char c, int cursorIndex) {
        // in which line
        // append character to the particular segment
        //currentSegmentIndex = getSegmentIndex(cursorIndex);
        //segments.get(currentSegmentIndex).appendContent(String.valueOf(c));
        printSegments();

    }

    public void backpress() {

        segments.get(currentSegmentIndex).truncate();
        printSegments();

    }

    private void printSegments() {
        int i = 0;
        l("----------------------------------");
        for (Segment segment : segments) {

            l("["+i+"] Start:"+ segment.startIndex+" End:"+segment.endIndex);

            i++;
        }
        l("----------------------------------");
    }

    class Segment {

        private int startIndex;
        private int endIndex = -1;
        public StringBuilder content;
        private Spannable spannedContent;
        private ArrayList<Integer> appliedSpans;

        public Segment(int startIndex) {

            this.startIndex = startIndex;
            content = new StringBuilder();
            appliedSpans = new ArrayList<>();
            spannedContent = new SpannableString("");
            l("Start Index:"+startIndex);
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void appendContent(String s) {
            l("Appending " + s);
            content.append(s);
            contentLen++;
            invalidateSpannedContent();
        }

        public void truncate() {
            l("Removing...");

            if (contentLen > 0) {
                content.setLength(contentLen - 1);
                contentLen--;
                invalidateSpannedContent();
            }

        }

        public String getContent() {
            return content.toString();
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        public void addSpan(int type) {

            l("Adding span type " + type);
            appliedSpans.add(type);
            invalidateSpannedContent();

        }

        public void removeSpan(int type) {

            appliedSpans.remove(type);
            invalidateSpannedContent();

        }

        private void invalidateSpannedContent() {

            l("reset spans");
            spannedContent = new SpannableString(content);
            endIndex = content.length() == 0 ? 0 : content.length();
            for (int i = 0; i < appliedSpans.size(); i++) {
                l("Applying spans " + startIndex + " to " + endIndex);
                spannedContent.setSpan(new AbsoluteSizeSpan(120), startIndex, endIndex, 0);
            }

        }

        @Override
        public String toString() {

            StringBuilder builder = new StringBuilder();
            builder.append("----------------------------------------------\n");
            builder.append(Html.toHtml(spannedContent));
            builder.append("----------------------------------------------");
            return builder.toString();

        }
    }

    private void l(String s) {
        Log.d("Segment", s);
    }
}
