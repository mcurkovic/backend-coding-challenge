"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy) {
	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	//var restExpenses = $restalchemy.init({ root: $config.apiroot, headers: {"Authorization": "Basic dXNlcjpwYXNzd29yZA=="}}).at("expenses");
	var restExpenses = $restalchemy.init({ root: $config.apiroot, headers: $config.authHeader }).at("expenses");
	var restCalculator = $restalchemy.init({ root: $config.apiroot, headers: $config.authHeader }).at("calculator");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	$scope.generalError = null;
	$scope.serverErrors = {};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
		});
	};

	$scope.parseAmount = function() {
		//reset fields
		$scope.newExpense.vat = null;
		$scope.newExpense.taxAmount = null;

		//set default values
		let amount = null;
		let currency = $config.defaultCurrencyCode;

		//parse amount and currency from input value - use select box for currency?
		const amountWithCurrency = $scope.newExpense.amountWithCurrency;
		if (amountWithCurrency) {
			$scope.newExpense.amountWithCurrency = amountWithCurrency.toUpperCase();
			const splittedValues = amountWithCurrency.split(" ");
			amount = splittedValues[0];
			//find enterd currency
			if (splittedValues.length > 1)
				currency = splittedValues[1];
			else { //no currency code entered: append it to amount
				$scope.newExpense.amountWithCurrency = $scope.newExpense.amountWithCurrency + " " + currency;
			}
		}
		//set parsed amount and currency
		$scope.newExpense.amount = amount;
		$scope.newExpense.currencyCode = currency;

		//calculate tax serverside
		if ($scope.newExpense.date && amount && currency) {
			restCalculator
				.post({
					amount: $scope.newExpense.amount,
					currencyCode: currency,
					date: $scope.newExpense.date
				}).then(function(result) {
					//reset custom form validations
					$scope.expensesform.date.$setValidity("serverError", true);
					$scope.generalError = null;
					//set calculated VAT
					$scope.newExpense.vat = result.amount + " " + result.currency;
					$scope.newExpense.taxAmount = result.amount;
				}).error(function(data) {
					//reset previous tax amount
					$scope.newExpense.vat = null;

					//set serverside validation error if any
					if (data && data.fieldErrors != null && data.fieldErrors.length > 0) {
						for (var i = 0; i < data.fieldErrors.length; i++) {
							const item = data.fieldErrors[i];
							$scope.expensesform[item.field].$setValidity("serverError", false);
							$scope.serverErrors[item.field] = item.message;
						}
					} else {
						//show general error
						$scope.generalError = data.message;
					}
				});
		}
	};

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
				loadExpenses();
				$scope.expensesform.$setPristine();
				$scope.newExpense = {};
				$scope.expensesform.$setUntouched();

			});
		}
	};

	$scope.clearExpense = function() {
		$scope.newExpense = {};
	};

	// Initialise scope variables
	loadExpenses();
	$scope.clearExpense();
}]);
