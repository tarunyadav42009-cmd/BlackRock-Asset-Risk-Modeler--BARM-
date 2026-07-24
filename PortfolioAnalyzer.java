import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortfolioAnalyzer {
    public static void main(String[] args) {

        List<Double> spyReturns = Arrays.asList(0.005, -0.002, 0.001, 0.008, -0.004);
        List<Double> bndReturns = Arrays.asList(0.001, 0.000089, -0.001, 0.002, 0.001);
        List<Double> gldReturns = Arrays.asList(-0.003, 0.004, 0.0045, -0.001, 0.005);

        // Portfolio weights: 60% SPY, 30% BND, 10% GLD
        double wSpy = 0.60, wBnd = 0.30, wGld = 0.10;
        double rfRate = 0.04; // 4% Risk-Free Rate

        List<Double> portfolioReturns = new ArrayList<>();

        // Calculating weighted daily return for the portfolio
        for (int i = 0; i < spyReturns.size(); i++) {
            double dailyPtfReturn = (spyReturns.get(i) * wSpy)
                    + (bndReturns.get(i) * wBnd)
                    + (gldReturns.get(i) * wGld);
            portfolioReturns.add(dailyPtfReturn);
        }

        // Metrics
        double annualizedVol = RiskEngine.calculateVolatility(portfolioReturns);

        // Mocking a 3-year annualized return of 8% for demonstration
        double simulatedAnnReturn = 0.08;
        double sharpeRatio = RiskEngine.calculateSharpe(simulatedAnnReturn, annualizedVol, rfRate);

        // Report
        System.out.println("---- BLACKROCK PORTFOLIO RISK REPORT ------");
        System.out.printf("Annualized Portfolio Volatility: %.2f%%\n", annualizedVol * 100);
        System.out.printf("Portfolio Sharpe Ratio         : %.2f\n", sharpeRatio);
        System.out.println("-----------------------");
    }
}
