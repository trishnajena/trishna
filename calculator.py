if operation == "add":
    result = num1 + num2
elif operation == "subtract":
    result = num1 - num2
elif operation == "multiply":
    result = num1 * num2
elif operation == "divide":
    if num2 == 0:
        return jsonify({"error": "Division by zero is not allowed"}), 400
    result = num1 / num2
else:
    return jsonify({"error": "Invalid operation"}), 400

return jsonify({"result": result})
