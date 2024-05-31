class CalculatorWrapper {
    static Calculator anonymousCalculator = new Calculator() {
        @Override
        public long sum(long val1, long val2) {
            return val1 + val2;
        }

        @Override
        public long subtraction(long val1, long val2) {
            return val1 - val2;
        }
    };

        /* create an instance of an anonymous class here,
                                            do not forget ; in the end */
}

abstract class Calculator {

    public abstract long sum(long val1, long val2);

    public abstract long subtraction(long val1, long val2);
}