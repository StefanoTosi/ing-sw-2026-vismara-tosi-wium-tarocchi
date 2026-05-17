package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Totem implements Serializable {
    ORANGE {
        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String getAscii(){return "\u001B[41m";}

        @Override
        public String getColor() {
            return "Orange";
        }
    },

    YELLOW {
        @Override
        public int getId() {
            return 1;
        }

        @Override
        public String getAscii(){return "\u001B[43m";}

        @Override
        public String getColor() {
            return "Yellow";
        }
    },

    BLUE {
        @Override
        public int getId() {
            return 2;
        }

        @Override
        public String getAscii(){return "\u001B[44m";}

        @Override
        public String getColor() {
            return "Blue";
        }
    },

    PURPLE {
        @Override
        public int getId() {
            return 3;
        }

        @Override
        public String getAscii(){return "\u001B[45m";}

        @Override
        public String getColor() {
            return "Purple";
        }
    },

    WHITE {
        @Override
        public int getId() {
            return 4;
        }
        
        @Override
        public String getAscii(){return "\u001B[47m";}

        @Override
        public String getColor() {
            return "White";
        }
    };

    public int getId() {
        return -1;
    }

    public String getAscii(){
        return "";
    }
    
    public String getColor() {
        return "";
    }
}
