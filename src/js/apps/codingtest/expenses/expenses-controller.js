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
	var restExpenses = $restalchemy.init({ root: $config.apiroot, headers: $config.authHeader}).at("expenses");
	var restCalculator = $restalchemy.init({ root: $config.apiroot, headers: $config.authHeader}).at("calculator");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
		});
	}

	$scope.parseAmount = function(){

		$scope.newExpense.vat = null
		$scope.newExpense.taxAmount = null

		let amount = null;
		//let currency = "GBP";

		let currency = $config.defaultCurrencyCode;
		console.log($config.defaultCurrencyCode)
		console.log($config)
		const amountWithCurrency = $scope.newExpense.amountWithCurrency;
		if (amountWithCurrency) {
			$scope.newExpense.amountWithCurrency = amountWithCurrency.toUpperCase()
			const splittedValues = amountWithCurrency.split(" ");
			amount = splittedValues[0]
			if (splittedValues.length > 1) {
				currency = splittedValues[1]
			}
		}

		$scope.newExpense.amount = amount
		$scope.newExpense.currencyCode = currency
		if ($scope.newExpense.date && amount && currency) {

			restCalculator.post({amount:$scope.newExpense.amount, currencyCode: currency , date: $scope.newExpense.date}).then(function(result) {
				// Reload new expenses list
				$scope.newExpense.vat = result.amount + " " + result.currency
				$scope.newExpense.taxAmount = result.amount
			});
		}
	}

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
				loadExpenses();
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
