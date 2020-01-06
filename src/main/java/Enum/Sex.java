package Enum;

public enum Sex {
    FEMALE {
        public String toString() {
            return "Female";
        }
    },
    MALE {
        public String toString() {
            return "Male";
        }
    };
    
}
