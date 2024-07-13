package br.com.rosa.domain.companyData.dto;

import br.com.rosa.domain.expenses.Expenses;

import java.util.List;

public record DataAccounting(
        double sumPaymentsMonth,
        double sumPaymentsYear,
        double valueExpensesMonth,
        double valueExpensesYear,
        List<Expenses> expensesList,
        List<DtoCommissionEmployee> commission
) {
    public DataAccounting(DataAccounting data) {
        this(data.sumPaymentsMonth, data.sumPaymentsYear, data.valueExpensesMonth, data.valueExpensesYear, data.expensesList, data.commission);
    }
}
