package sim.subd;

public enum Quality {
    Super_star{
        @Override
        public Double getMax(){
            return 0.009;
        }
        @Override
        public Double getMin(){
            return -0.006;
        }
    }, Star{
        @Override
        public Double getMax(){
            return 0.008;
        }
        @Override
        public Double getMin() {
            return -0.007;
        }
    }, Average{
        public Double getMax(){
            return 0.007;
        }
        @Override
        public Double getMin() {
            return -0.008;
        }

    }, Newby{
        public Double getMax(){
            return 0.008;
        }
        @Override
        public Double getMin() {
            return -0.009;
        }
    }, Wunderkind{
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
