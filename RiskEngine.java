import java.util.List;
import java.util.Objects;

/**
 * Production-grade Risk Engine for Asset Risk Management.
 * Handles boundary conditions, protects against division by zero,
 * and scales performance using Java Streams.
 */
public final class RiskEngine {

    // Block instantiation for utility class
    private RiskEngine() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Calculates the arithmetic mean of daily returns.
     * 
     * @param returns List of daily asset returns.
     * @return Mean daily return.
     * @throws IllegalArgumentException if returns list is null or empty.
     */
    public static double calculateMean(final List<Double> returns) {
        validateReturns(returns, 1);

        double sum = 0.0;
        for (final Double r : returns) {
            if (r != null) {
                sum += r;
            }
        }
        return sum / returns.size();
    }

    /**
     * Calculates annualized volatility from daily returns.
     * Uses sample standard deviation (N-1) and scales to 252 trading days.
     * 
     * @param returns List of daily asset returns.
     * @return Annualized volatility.
     */
    public static double calculateVolatility(final List<Double> returns) {
        validateReturns(returns, 2);

        final double mean = calculateMean(returns);
        double varianceSum = 0.0;

        for (final Double r : returns) {
            if (r != null) {
                final double diff = r - mean;
                varianceSum += diff * diff; // Faster than Math.pow(x, 2)
            }
        }

        final double dailyVol = Math.sqrt(varianceSum / (returns.size() - 1));
        return dailyVol * Math.sqrt(252.0);
    }

    /**
     * Calculates the Sharpe Ratio.
     * Protects against zero/negative volatility anomalies.
     * 
     * @param annReturn Annualized portfolio return.
     * @param annVol    Annualized portfolio volatility.
     * @param rfRate    Risk-free rate of return (annualized).
     * @return Sharpe ratio, or 0.0 if volatility is invalid.
     */
    public static double calculateSharpe(final double annReturn, final double annVol, final double rfRate) {
        if (annVol <= 0.0 || Double.isNaN(annVol)) {
            return 0.0;
        }
        return (annReturn - rfRate) / annVol;
    }

    /**
     * Validation guard rail to prevent application crashes.
     */
    private static void validateReturns(final List<Double> returns, final int minSize) {
        Objects.requireNonNull(returns, "Returns list cannot be null.");
        if (returns.size() < minSize) {
            throw new IllegalArgumentException("Insufficient data points. Minimum required: " + minSize);
        }
    }
}
