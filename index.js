var displayArea = document.getElementById("display");

function isOperator(text) {
  if (text == "+" || text == "-" || text == "*" || text == "/" || text == "%")
    return true;
  return false;
}

function buttonClicked(buttonText) {
  if (displayArea.innerText.length >= 30) return;
  if (buttonText != "AC" && buttonText != "Del" && buttonText != "=") {
    if (displayArea.innerText != "0") {
      if (
        isOperator(displayArea.innerText[displayArea.innerText.length - 1]) &&
        isOperator(buttonText)
      ) {
        displayArea.innerText = displayArea.innerText.slice(0, -1) + buttonText;
      } else displayArea.innerText = displayArea.innerText + buttonText;
    } else {
      if (buttonText != "00" && buttonText != "0") {
        if (buttonText == "." || isOperator(buttonText)) {
          displayArea.innerText = displayArea.innerText + buttonText;
        } else displayArea.innerText = buttonText;
      }
    }
  } else {
    if (buttonText === "AC") {
      displayArea.innerText = "0";
    } else if (buttonText === "Del") {
      if (
        displayArea.innerText.slice(0, -1) == "" ||
        displayArea.innerText === "Infinity" ||
        displayArea.innerText === "NaN"
      )
        displayArea.innerText = "0";
      else displayArea.innerText = displayArea.innerText.slice(0, -1);
    } else if (buttonText === "=") {
      try {
        displayArea.innerText = eval(displayArea.innerText);
      } catch (error) {
        displayArea.innerText = "0";
      }
    }
  }
}
