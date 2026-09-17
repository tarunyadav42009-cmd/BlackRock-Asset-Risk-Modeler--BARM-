import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Production-grade Portfolio Analyzer.
 * Validates portfolio construction rules, processes multi-asset return paths,
 * and formats performance data for executive reporting.
 */
public final class PortfolioAnalyzer {

    private static final double RISK_FREE_RATE = 0.04;
    private static final double SIMULATED_ANNUAL_RETURN = 0.08;
    private static final double WEIGHT_TOLERANCE = 1e-6;

    public static void main(final String[] args) {
        // 1. Initialize asset daily returns using immutable lists
        final List<Double> spyReturns = List.of(0.005, -0.002, 0.001, 0.008, -0.004);
        final List<Double> bndReturns = List.of(0.001, 0.000089, -0.001, 0.002, 0.001);
        final List<Double> gldReturns = List.of(-0.003, 0.004, 0.0045, -0.001, 0.005);

        // 2. Map allocations dynamically for simpler matrix operations
        final Map<String, Double> allocations = Map.of(
                "SPY", 0.60,
                "BND", 0.30,
                "GLD", 0.10);

        // 3. System Guard: Enforce fundamental portfolio weight math
        validatePortfolioWeights(allocations);

        // 4. System Guard: Confirm alignment of data arrays
        final int dataPoints = spyReturns.size();
        if (bndReturns.size() != dataPoints || gldReturns.size() != dataPoints) {
            throw new IllegalArgumentException("Asset return histories are misaligned. Matrix sizes must match.");
        }

        // 5. Compute matrix products for weighted daily returns
        final List<Double> portfolioReturns = new ArrayList<>(dataPoints);
        for (int i = 0; i < dataPoints; i++) {
            final double spyComponent = Objects.requireNonNullElse(spyReturns.get(i), 0.0) * allocations.get("SPY");
            final double bndComponent = Objects.requireNonNullElse(bndReturns.get(i), 0.0) * allocations.get("BND");
            final double gldComponent = Objects.requireNonNullElse(gldReturns.get(i), 0.0) * allocations.get("GLD");

            portfolioReturns.add(spyComponent + bndComponent + gldComponent);
        }

        // 6. Generate final engine metrics
        final double annualizedVol = RiskEngine.calculateVolatility(portfolioReturns);
        final double sharpeRatio = RiskEngine.calculateSharpe(SIMULATED_ANNUAL_RETURN, annualizedVol, RISK_FREE_RATE);

        // 7. Output production telemetry report
        printInstitutionalReport(allocations, annualizedVol, sharpeRatio);
    }

    /**
     * Assures asset weights sum to exactly 1.00 (100%) within standard double
     * floating point margins.
     */
    private static void validatePortfolioWeights(final Map<String, Double> allocations) {
        final double totalWeight = allocations.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalWeight - 1.0) > WEIGHT_TOLERANCE) {
            throw new IllegalStateException(String
                    .format("Invalid allocation matrix. Total weight must equal 1.00 (Current: %.4f)", totalWeight));
        }
    }

    /**
     * Standardized BlackRock-style Performance and Compliance Report.
     */
    private static void printInstitutionalReport(final Map<String, Double> allocations, final double volatility,
            final double sharpe) {
        System.out.println("=================================================");
        System.out.println("         BLACKROCK PORTFOLIO RISK REPORT         ");
        System.out.println("=================================================");
        System.out.println("PORTFOLIO ALLOCATION:");
        allocations.forEach((asset, weight) -> System.out.printf("  • %-5s : %.1f%%\n", asset, weight * 100));
        System.out.println("-------------------------------------------------");
        System.out.println("PERFORMANCE METRICS:");
        System.out.printf("  Assumed Risk-Free Rate       : %.2f%%\n", RISK_FREE_RATE * 100);
        System.out.printf("  Simulated Annualised Return   : %.2f%%\n", SIMULATED_ANNUAL_RETURN * 100);
        System.out.printf("  Annualised Portfolio Vol (σ) : %.2f%%\n", volatility * 100);
        System.out.printf("  Portfolio Sharpe Ratio        : %.2f\n", sharpe);
        System.out.println("=================================================");
    }
}
