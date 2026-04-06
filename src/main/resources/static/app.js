const BASE = "http://localhost:8080";

// CREATE ACCOUNT
function createAccount() {

    if (!accName.value.trim()) {
        alert("Account name is required");
        return;
    }

    fetch(BASE + "/accounts", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            name: accName.value,
            type: accType.value,
            balance: 0
        })
    }).then(() => alert("Account created"));
}

// LOAD ACCOUNTS
function loadAccounts() {
    fetch(BASE + "/accounts")
        .then(res => res.json())
        .then(data => {
            accounts.innerHTML = "";
            data.forEach(a => {
                let li = document.createElement("li");
                li.innerText = `${a.id} | ${a.name} | ${a.balance}`;
                accounts.appendChild(li);
            });
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
    }).then(() => alert("Transaction created"));
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
            statement.innerHTML = "";
            data.forEach(e => {
                let li = document.createElement("li");
                li.innerText = `${e.date} | ${e.title} | ${e.type} | ${e.amount}`;
                statement.appendChild(li);
            });
        });
}

// SUMMARY
function loadSummary() {
    fetch(BASE + `/accounts/${sumAccId.value}/summary?month=${month.value}&year=${year.value}`)
        .then(res => res.json())
        .then(data => {
            summary.innerText = JSON.stringify(data, null, 2);
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
    }).then(() => alert("Reversed"));
}

// TRANSACTION ENTRIES
function loadEntries() {
    fetch(BASE + `/transactions/${entryTxnId.value}/entries`)
        .then(res => res.json())
        .then(data => {
            entriesList.innerHTML = "";
            data.forEach(e => {
                let li = document.createElement("li");
                li.innerText = `${e.account.name} | ${e.type} | ${e.amount} | ${e.transaction.createdAt}`;
                entriesList.appendChild(li);
            });
        });
}