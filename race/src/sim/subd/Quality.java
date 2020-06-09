package sim.subd;

public enum Quality {
    SUPERSTAR{
        @Override
        public Double getMax(){
            return 0.009;
        }
        @Override
        public Double getMin(){
            return -0.006;
        }
    }, STAR{
        @Override
        public Double getMax(){
            return 0.008;
        }
        @Override
        public Double getMin() {
            return -0.007;
        }
    }, AVERAGE{
        public Double getMax(){
            return 0.007;
        }
        @Override
        public Double getMin() {
            return -0.008;
        }

    }, NEWBY{
        public Double getMax(){
            return 0.008;
        }
        @Override
        public Double getMin() {
            return -0.009;
        }
    }, WUNDERKIND{
        public Double getMax(){
            return 0.009;
        }
        @Override
        public Double getMin() {
            return -0.009;
        }

    };
    public abstract Double getMax();
    public abstract Double getMin();

}
