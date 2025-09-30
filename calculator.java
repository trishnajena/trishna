let input = "";
let history = [];
let historyIndex = -1;
let afterEqual = false;

function appendValue(val) {
  if (afterEqual) {
    let previousResult = document.getElementById('resultDisplay').innerText;

    if (/[0-9.]/.test(val)) {
      input = "";
      document.getElementById('inputDisplay').innerText = "";
    } else if (/sin\(|cos\(|tan\(|√\(/.test(val)) {
      if (val === '√(') {
        input = 'Math.sqrt(' + previousResult + ")";
        document.getElementById('inputDisplay').innerText = '√(' + previousResult + ")";
      } else {
        input = val + previousResult + ")";
        document.getElementById('inputDisplay').innerText = val + previousResult + ")";
      }
      afterEqual = false;
      return;
    } else if (/[\+\-\*\/\^]/.test(val)) {
      input = previousResult;
      document.getElementById('inputDisplay').innerText = input;
    }
    afterEqual = false;
  }

  if (val === 'π') {
    input += 'Math.PI';
    document.getElementById('inputDisplay').innerText += 'π';
  } else if (val === '.') {
    const parts = input.split(/[\+\-\*\/\(\)%]/);
    const lastPart = parts[parts.length - 1];
    if (lastPart.includes('.')) return;
    input += val;
    document.getElementById('inputDisplay').innerText += val;
  } else if (val === '*') {
    input += '*';
    document.getElementById('inputDisplay').innerText += 'x';
  } else if (val === '√(') {
    input += 'Math.sqrt(';
    document.getElementById('inputDisplay').innerText += '√(';
  } else {
    input += val;
    document.getElementById('inputDisplay').innerText += val;
  }
}

function clearDisplay() {
  input = "";
  document.getElementById('inputDisplay').innerText = "";
  document.getElementById('resultDisplay').innerText = "";
}

function backspace() {
  const displayText = document.getElementById('inputDisplay').innerText;
  if (displayText.endsWith('sin(') || displayText.endsWith('cos(') || displayText.endsWith('tan(')) {
    input = input.slice(0, input.length - 4);
    document.getElementById('inputDisplay').innerText = displayText.slice(0, -4);
  } else if (displayText.endsWith('√(')) {
    input = input.slice(0, input.length - 9);
    document.getElementById('inputDisplay').innerText = displayText.slice(0, -2);
  } else {
    input = input.slice(0, -1);
    document.getElementById('inputDisplay').innerText = displayText.slice(0, -1);
  }
}

function calculateResult() {
  try {
    let evalInput = input;
    evalInput = evalInput.replace(/sin\(/g, "Math.sin(")
                         .replace(/cos\(/g, "Math.cos(")
                         .replace(/tan\(/g, "Math.tan(");
    const mode = document.querySelector('input[name="mode"]:checked').value;
    if (mode === "deg") {
      evalInput = evalInput.replace(/Math\.sin\(([^)]+)\)/g, "Math.sin(($1)*Math.PI/180)");
      evalInput = evalInput.replace(/Math\.cos\(([^)]+)\)/g, "Math.cos(($1)*Math.PI/180)");
      evalInput = evalInput.replace(/Math\.tan\(([^)]+)\)/g, "Math.tan(($1)*Math.PI/180)");
    }
    evalInput = evalInput.replace(/(\d+(\.\d+)?)%/g, "($1/100)");
    evalInput = evalInput.replace(/\^/g, "**");

    let result = eval(evalInput);
    if (!isFinite(result) || Math.abs(result) > 1e10) throw "Math Error";
    if (Math.abs(result) < 1e-10) result = 0;
    result = parseFloat(result.toFixed(10));

    document.getElementById('resultDisplay').innerText = result;
    history.push({ input: document.getElementById('inputDisplay').innerText, result: result });
    historyIndex = history.length - 1;
    afterEqual = true;
  } catch (e) {
    document.getElementById('resultDisplay').innerText = "Error";
  }
}

function previousHistory() {
  if (historyIndex > 0) {
    historyIndex--;
    showHistory();
  }
}

function nextHistory() {
  if (historyIndex < history.length - 1) {
    historyIndex++;
    showHistory();
  }
}

function showHistory() {
  const h = history[historyIndex];
  document.getElementById('inputDisplay').innerText = h.input;
  document.getElementById('resultDisplay').innerText = h.result;
  input = "";
}

document.addEventListener('keydown', function(e) {
  const panel = document.getElementById('historyPanel');
  if (panel.classList.contains('visible')) {
    panel.classList.remove('visible');
  }

  const key = e.key;
  const allowedKeys = [
    '0','1','2','3','4','5','6','7','8','9',
    '+','-','*','/','^','.','p','π','%','(',')',
    's','c','t',
    'Enter','=', 'Backspace','Escape','ArrowUp','ArrowDown'
  ];

  if (!allowedKeys.includes(key)) {
    e.preventDefault();
    return;
  }

  if (/[0-9]/.test(key)) {appendValue(key);}
  else if (key === '+') {appendValue('+');}
  else if (key === '-') {appendValue('-');}
  else if (key === '*') {appendValue('*');}
  else if (key === '/') {appendValue('/');}
  else if (key === '^') {appendValue('^');}
  else if (key === '.') {appendValue('.');}
  else if (key === '%') {appendValue('%');}
  else if (key === '(') {appendValue('(');}
  else if (key === ')') {appendValue(')');}
  else if (key === 'π' || key === 'p') {appendValue('π');}
  else if (key === 's') {appendValue('sin(');}
  else if (key === 'c') {appendValue('cos(');}
  else if (key === 't') {appendValue('tan(');}
  else if (key === 'Enter' || key === '=') {calculateResult();}
  else if (key === 'Backspace') {backspace();}
  else if (key === 'Escape') {clearDisplay();}
  else if (key === 'ArrowUp') {previousHistory();}
  else if (key === 'ArrowDown') {nextHistory();}

  e.preventDefault();
});

function toggleHistory() {
  const panel = document.getElementById('historyPanel');
  panel.classList.toggle('visible');
  updateFullHistoryList();
}

function updateFullHistoryList() {
  const ul = document.getElementById('historyList');
  ul.innerHTML = "";
  history.forEach((entry, index) => {
    const li = document.createElement('li');
    li.innerHTML = `
      <span class="history-item">${entry.input} = ${entry.result}</span>
      <span class="delete-btn" onclick="event.stopPropagation(); deleteHistoryItem(${index})">❌</span>
    `;
    li.querySelector('.history-item').addEventListener('click', () => {
      input += entry.result.toString();
      document.getElementById('inputDisplay').innerText += entry.result.toString();
      afterEqual = false;
    });
    ul.appendChild(li);
  });
}

function clearFullHistory() {
  history = [];
  historyIndex = -1;
  updateFullHistoryList();
}

function deleteHistoryItem(index) {
  history.splice(index, 1);
  historyIndex = history.length - 1;
  updateFullHistoryList();
}

document.addEventListener('click', function(event) {
  const panel = document.getElementById('historyPanel');
  const toggleBtn = document.getElementById('historyToggle');

  if (!panel.contains(event.target) && !toggleBtn.contains(event.target)) {
    panel.classList.remove('visible');
  }
});
