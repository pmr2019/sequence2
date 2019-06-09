package com.example.td_wang_yang_wei;

import java.util.List;

public class Lists {

    /**
     * version : 1
     * success : true
     * status : 200
     * lists : [{"id":"46","label":"ll"},{"id":"26","label":"w1"}]
     */

    private int version;
    private boolean success;
    private int status;
    private List<ListsBean> lists;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public static class ListsBean {
        /**
         * id : 46
         * label : ll
         */

        private String id;
        private String label;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}

