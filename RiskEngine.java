import java.util.List;

public class RiskEngine {

    // Calculating mean (average) of daily returns
    public static double calculateMean(List<Double> returns) {
        double sum = 0;
        for (double r : returns) {
            sum += r;
        }
        return sum / returns.size();
    }

    // Calculating Standard Deviation (Volatility)
    public static double calculateVolatility(List<Double> returns) {
        double mean = calculateMean(returns);
        double temp = 0;
        for (double r : returns) {
            temp += Math.pow(r - mean, 2);
        }
        // standard deviation
        double dailyVol = Math.sqrt(temp / (returns.size() - 1));

        return dailyVol * Math.sqrt(252);
    }

    // Sharpe Ratio
    public static double calculateSharpe(double annReturn, double annVol, double rfRate) {
        return (annReturn - rfRate) / annVol;
    }
}
