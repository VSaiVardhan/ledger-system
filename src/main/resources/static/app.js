const BASE = "https://ledger-system-df4y.onrender.com";

// CREATE ACCOUNT 
function createAccount() {

    if (!accName.value.trim()) {
        alert("Account name is required");
        return;
    }

    // Get balance from input
    let balanceValue = accBalance.value;

    // Validation (optional but recommended)
    if (balanceValue && isNaN(balanceValue)) {
        alert("Balance must be a valid number");
        return;
    }

    fetch(BASE + "/accounts", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            name: accName.value,
            type: accType.value,
            balance: balanceValue ? parseFloat(balanceValue) : 0
        })
    })
    .then(res => res.json())
    .then(data => {
        console.log("Account created:", data);
        alert("Account created successfully!");

        // Optional UX improvement
        accName.value = "";
        accBalance.value = "";
    })
    .catch(err => {
        console.error("Error:", err);
        alert("Error creating account");
    });
}

// LOAD ACCOUNTS
function loadAccounts() {
    fetch(BASE + "/accounts")
        .then(res => res.json())
        .then(data => {
            console.log("Accounts:", data);
            accounts.innerHTML = "";
            data.forEach(a => {
                let li = document.createElement("li");
                li.innerText = `${a.id} | ${a.name} | ${a.balance}`;
                accounts.appendChild(li);
            });
        })
        .catch(err => {
            console.error("Error loading accounts:", err);
        });
}

// CREATE TRANSACTION
function createTransaction() {

    if (!desc.value.trim() || !date.value) {
        alert("Description and date are required");
        return;
    }

    if (!acc1.value || !acc2.value || !amt1.value || !amt2.value) {
        alert("All entry fields must be filled");
        return;
    }

    if (isNaN(acc1.value) || isNaN(acc2.value)) {
        alert("Account IDs must be numbers");
        return;
    }

    if (isNaN(amt1.value) || isNaN(amt2.value)) {
        alert("Amounts must be valid numbers");
        return;
    }

    if (parseFloat(amt1.value) <= 0 || parseFloat(amt2.value) <= 0) {
        alert("Amounts must be greater than 0");
        return;
    }

    fetch(BASE + "/transactions/double", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            description: desc.value,
            date: date.value,
            entries: [
                {
                    accountId: parseInt(acc1.value),
                    type: type1.value,
                    amount: parseFloat(amt1.value)
                },
                {
                    accountId: parseInt(acc2.value),
                    type: type2.value,
                    amount: parseFloat(amt2.value)
                }
            ]
        })
    })
    .then(res => res.text())
    .then(data => {
        console.log("Transaction response:", data);
        alert("Transaction created!");
    })
    .catch(err => {
        console.error("Error:", err);
        alert("Error creating transaction");
    });
}

// STATEMENT
function loadStatement() {
    let url = BASE + `/accounts/${stmtAccId.value}/statement`;

    if (from.value && to.value) {
        url += `?from=${from.value}&to=${to.value}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(data => {
            console.log("Statement:", data);
            statement.innerHTML = "";
            data.forEach(e => {
                let li = document.createElement("li");
                li.innerText = `${e.date} | ${e.title} | ${e.type} | ${e.amount}`;
                statement.appendChild(li);
            });
        })
        .catch(err => {
            console.error("Error loading statement:", err);
        });
}

// SUMMARY
function loadSummary() {

    if (!sumAccId.value || isNaN(sumAccId.value)) {
        alert("Enter a valid Account ID");
        return;
    }

    if (!monthYear.value) {
        alert("Please select a month");
        return;
    }

    // extract from month picker
    let [yearVal, monthVal] = monthYear.value.split("-");

    fetch(BASE + `/accounts/${sumAccId.value}/summary?month=${monthVal}&year=${yearVal}`)
        .then(res => res.json())
        .then(data => {
            summary.innerText = `
Total Debit        : ₹${data.totalDebit}
Total Credit       : ₹${data.totalCredit}
Net Balance Change : ₹${data.netBalanceChange}
            `;
        })
        .catch(err => {
            console.error(err);
            alert("Failed to load summary");
        });
}

// REVERSAL
function reverseTransaction() {

    if (!txnId.value || isNaN(txnId.value)) {
        alert("Enter a valid Transaction ID");
        return;
    }

    fetch(BASE + `/transactions/${txnId.value}/reverse`, {
        method: "POST"
    })
    .then(res => res.text())
    .then(data => {
        console.log("Reversal:", data);
        alert("Transaction reversed!");
    })
    .catch(err => {
        console.error("Error reversing:", err);
        alert("Error reversing transaction");
    });
}

// TRANSACTION ENTRIES
function loadEntries() {
    fetch(BASE + `/transactions/${entryTxnId.value}/entries`)
        .then(res => res.json())
        .then(data => {
            console.log("Entries:", data);
            entriesList.innerHTML = "";
            data.forEach(e => {
                let li = document.createElement("li");
                li.innerText = `${e.account.name} | ${e.type} | ${e.amount} | ${e.transaction.createdAt}`;
                entriesList.appendChild(li);
            });
        })
        .catch(err => {
            console.error("Error loading entries:", err);
        });
}

// TRANSACTION HISTORY
function loadTransactions() {

    let url = BASE + "/transactions";

    // Apply date filter if present
    if (txnFrom.value && txnTo.value) {
        url += `?from=${txnFrom.value}&to=${txnTo.value}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(data => {
            console.log("Transactions:", data);

            transactionsList.innerHTML = "";

            data.forEach(t => {
                let li = document.createElement("li");

                li.innerText = `ID: ${t.id} | ${t.title} | ${t.date}`;

                //  CLICKABLE
                li.style.cursor = "pointer";

                li.onclick = () => {
                    loadEntriesByTransaction(t.id);
                };

                transactionsList.appendChild(li);
            });
        })
        .catch(err => console.error(err));
}

function loadEntriesByTransaction(txnId) {

    fetch(BASE + `/transactions/${txnId}/entries`)
        .then(res => res.json())
        .then(data => {
            console.log("Entries:", data);

            entriesList.innerHTML = "";

            data.forEach(e => {
                let li = document.createElement("li");

                li.innerText = `${e.account.name} | ${e.type} | ${e.amount}`;

                entriesList.appendChild(li);
            });
        })
        .catch(err => console.error(err));
}

window.createAccount = createAccount;
window.loadAccounts = loadAccounts;
window.createTransaction = createTransaction;
window.loadStatement = loadStatement;
window.loadSummary = loadSummary;
window.reverseTransaction = reverseTransaction;
window.loadEntries = loadEntries;
window.loadTransactions = loadTransactions;
window.loadEntriesByTransaction = loadEntriesByTransaction;