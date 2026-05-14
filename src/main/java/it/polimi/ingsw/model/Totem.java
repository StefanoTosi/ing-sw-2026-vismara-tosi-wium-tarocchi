package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Totem implements Serializable {
    ORANGE {
        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String getColor() {
            return "";
        }
    },

    YELLOW {
        @Override
        public int getId() {
            return 1;
        }

        @Override
        public String getColor() {
            return "";
        }
    },

    BLUE {
        @Override
        public int getId() {
            return 2;
        }

        @Override
        public String getColor() {
            return "";
        }
    },

    PURPLE {
        @Override
        public int getId() {
            return 3;
        }

        @Override
        public String getColor() {
            return "";
        }
    },

    WHITE {
        @Override
        public int getId() {
            return 4;
        }

        @Override
        public String getColor() {
            return "";
        }
    };

    public int getId() {
        return -1;
    }

    public String getColor() {
        return "";
    }
}
