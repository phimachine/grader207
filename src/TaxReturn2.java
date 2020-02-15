import java.util.*;

public class TaxReturn2 {
    public static void main(String[] args) {

        final double RATE1 = 0.15;
        final double RATE2 = 0.28;
        final double RATE3 = 0.31;

        final double SINGLE_BRACKET1 = 21450;
        final double SINGLE_BRACKET2 = 51900;

        final double MARRIED_BRACKET1 = 35800;
        final double MARRIED_BRACKET2 = 86500;

        Scanner stdin = new Scanner(System.in);

        System.out.print("Enter Filing status. Type single or married: ");
        String status = stdin.next();
        if (!status.equals("single") && !status.equals("married")) {
            System.out.println("Invalid Input");// Done here if Invalid status
        } else {// Begin more processing
            System.out.print("Enter number of dependents: ");
            if (stdin.hasNextInt()) {
                int dependents = stdin.nextInt();// read dependents
                if (dependents >= 0) {
                    // Input (status and dependents) is valid: More processing
                    if (status.equals("single")) {// status is single
                        System.out.print("Enter annual income: ");
                        if (stdin.hasNextDouble()) {
                            // income is a real number
                            double income = stdin.nextDouble();
                            if (income >= 0) {
                                int exemptions = dependents + 1;
                                double adjustedIncome = 0.0;
                                if (exemptions < 5)
                                    adjustedIncome = income - exemptions * 5000;
                                else
                                    adjustedIncome = income - exemptions * 4500;

                                double tax = 0;// Initialize tax to zero
                                // Compute the appropriate tax for single

                                if (adjustedIncome <= SINGLE_BRACKET1) {
                                    tax = adjustedIncome * RATE1;
                                } else if (adjustedIncome <= SINGLE_BRACKET2) {
                                    tax = SINGLE_BRACKET1 * RATE1 + (adjustedIncome - SINGLE_BRACKET1) * RATE2;
                                } else {
                                    tax = SINGLE_BRACKET1 * RATE1 + (SINGLE_BRACKET2 - SINGLE_BRACKET1) * RATE2
                                            + (adjustedIncome - SINGLE_BRACKET2) * RATE3;
                                } // end if else
                                // Printing the Output
                                tax = Math.max(0, tax);
                                System.out.printf("%-20s", "Filing Status");
                                System.out.printf("%-22s", "Annual Gross Income ");
                                System.out.printf("%-13s", "Dependents");
                                System.out.printf("%-12s", "Tax Owed");
                                System.out.println();
                                System.out.printf("%-20s", status);
                                System.out.printf("%,-22.2f", income);
                                System.out.printf("%-13d", dependents);
                                System.out.printf("%,-12.2f", tax);
                            } else {
                                // income is negative, so invalid
                                System.out.println("Invalid input");
                            } // end if income > 0
                        } else {
                            System.out.println("Invalid Input");
                        } // end hasNextDouble for income

                    } // end if part for status being single
                    else { // status is married
                        System.out.print("Enter your income: ");
                        if (stdin.hasNextDouble()) {
                            double income1 = stdin.nextDouble();
                            System.out.print("Enter spouse's income: ");
                            if (stdin.hasNextDouble()) {
                                double income2 = stdin.nextDouble();
                                if (income1 >= 0 && income2 >= 0) {
                                    // All inputs are valid
                                    double AGI = income1 + income2;
                                    double tax = 0.0;
                                    double adjustedIncome = 0.0;
                                    int exemptions = dependents + 2;
                                    if (exemptions < 5)
                                        adjustedIncome = AGI - exemptions * 5000;
                                    else
                                        adjustedIncome = AGI - exemptions * 4500;
                                    // Compute appropriate tax for married

                                    if (adjustedIncome <= MARRIED_BRACKET1) {
                                        tax = adjustedIncome * RATE1;
                                    } else if (adjustedIncome <= MARRIED_BRACKET2) {
                                        tax = MARRIED_BRACKET1 * RATE1 + (adjustedIncome - MARRIED_BRACKET1) * RATE2;
                                    } else {
                                        tax = MARRIED_BRACKET1 * RATE1 + (MARRIED_BRACKET2 - MARRIED_BRACKET1) * RATE2
                                                + (adjustedIncome - MARRIED_BRACKET2) * RATE3;
                                    } // end else if

                                    // Printing the Output
                                    tax = Math.max(0, tax);
                                    System.out.printf("%-20s", "Filing Status");
                                    System.out.printf("%-22s", "Annual Gross Income ");
                                    System.out.printf("%-13s", "Dependents");
                                    System.out.printf("%-12s", "Tax Owed");
                                    System.out.println();
                                    System.out.printf("%-20s", status);
                                    System.out.printf("%,-22.2f", AGI);
                                    System.out.printf("%-13d", dependents);
                                    System.out.printf("%,-12.2f", tax);
                                } else {
                                    // Incomes are negative numbers
                                    System.out.println("Invalid input");
                                } // end if incomes are < 0
                            } else {
                                // income2 is not double
                                System.out.println("Invalid Input");
                            } // end if income2 is not double
                        } else {
                            // income1 is not double
                            System.out.println("Invalid input");
                        } // end if income1 is not double
                    } // end else Status being married

                } // end if part of dependents >= 0
                else {
                    // dependents is negative
                    System.out.println("Invalid Input");
                } // end else part of dependents >=0
            } // end if part of dependents being integer
            else {
                // Dependents is not an integer
                System.out.println("Invalid Input");
            } // end else part of dependents not integer
        } // end if Valid status
        stdin.close();
    }// end main


    public static TaxReport computeTax(boolean single, double singleIncome, double myIncome,
									   double spouseIncome, int dependents) {

		final double RATE1 = 0.15;
		final double RATE2 = 0.28;
		final double RATE3 = 0.31;

		final double SINGLE_BRACKET1 = 21450;
		final double SINGLE_BRACKET2 = 51900;

		final double MARRIED_BRACKET1 = 35800;
		final double MARRIED_BRACKET2 = 86500;

		Scanner stdin = new Scanner(System.in);


		String status;
		if (single) {
			status = "single";
		} else {
			status = "married";
		}

		// Input (status and dependents) is valid: More processing
		if (status.equals("single")) {// status is single
			// income is a real number
			double income = singleIncome;
			int exemptions = dependents + 1;
			double adjustedIncome = 0.0;
			if (exemptions < 5)
				adjustedIncome = income - exemptions * 5000;
			else
				adjustedIncome = income - exemptions * 4500;

			double tax = 0;// Initialize tax to zero
			// Compute the appropriate tax for single

			if (adjustedIncome <= SINGLE_BRACKET1) {
				tax = adjustedIncome * RATE1;
			} else if (adjustedIncome <= SINGLE_BRACKET2) {
				tax = SINGLE_BRACKET1 * RATE1 + (adjustedIncome - SINGLE_BRACKET1) * RATE2;
			} else {
				tax = SINGLE_BRACKET1 * RATE1 + (SINGLE_BRACKET2 - SINGLE_BRACKET1) * RATE2
						+ (adjustedIncome - SINGLE_BRACKET2) * RATE3;
			} // end if else
			// Printing the Output
			tax = Math.max(0, tax);
			return new TaxReport(single, income, dependents, tax);
		}
		else{ // status is married
			double income1 = myIncome;
			double income2 = spouseIncome;
			// All inputs are valid
			double AGI = income1 + income2;
			double tax = 0.0;
			double adjustedIncome = 0.0;
			int exemptions = dependents + 2;
			if (exemptions < 5)
				adjustedIncome = AGI - exemptions * 5000;
			else
				adjustedIncome = AGI - exemptions * 4500;
			// Compute appropriate tax for married

			if (adjustedIncome <= MARRIED_BRACKET1) {
				tax = adjustedIncome * RATE1;
			} else if (adjustedIncome <= MARRIED_BRACKET2) {
				tax = MARRIED_BRACKET1 * RATE1 + (adjustedIncome - MARRIED_BRACKET1) * RATE2;
			} else {
				tax = MARRIED_BRACKET1 * RATE1 + (MARRIED_BRACKET2 - MARRIED_BRACKET1) * RATE2
						+ (adjustedIncome - MARRIED_BRACKET2) * RATE3;
			} // end else if

			// Printing the Output
			tax = Math.max(0, tax);
			return new TaxReport(single, AGI, dependents, tax);
		} // end else Status being married
	}
}// end class TaxReturn

