const BASE = "http://localhost:8080";

// CREATE ACCOUNT
function createAccount() {
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
    fetch(BASE + `/transactions/${txnId.value}/reverse`, {
        method: "POST"
    }).then(() => alert("Reversed"));
}