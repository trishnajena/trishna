# --- Calculator Logic ---
class Calculator:
    def __init__(self):
        # Stores the expression shown on the screen
        self.expression = ""
        
    def append_to_expression(self, value):
        """Adds a number or operator to the expression string."""
        self.expression += str(value)
        return self.expression

    def clear_expression(self):
        """Clears the entire expression."""
        self.expression = ""
        return self.expression
        
    def calculate(self):
        """Evaluates the mathematical expression."""
        try:
            # NOTE: eval() is powerful but should be used with caution in real-world apps.
            # For a basic calculator, it's the simplest way to handle math strings.
            result = str(eval(self.expression))
            self.expression = result
            return result
        except ZeroDivisionError:
            self.expression = ""
            return "Error: Div/0"
        except Exception:
            self.expression = ""
            return "Error"

# --- GUI Setup ---
class CalculatorApp:
    def __init__(self, master):
        self.master = master
        master.title("Professional Calculator")
        master.config(bg="#1c1c1c") # Dark background for the app

        # Initialize the calculation engine
        self.calc = Calculator()
        
        # Variable to hold the display text
        self.display_text = tk.StringVar()
        self.display_text.set("0")
        
        # Setup the display (Entry widget)
        self.display = tk.Entry(master, textvariable=self.display_text, font=('Arial', 24, 'bold'),
                                bd=0, relief='flat', bg="#3a3a3a", fg="white", justify='right')
        self.display.grid(row=0, column=0, columnspan=4, padx=10, pady=10, ipady=10, sticky="nsew")
        
        # Configure grid for resizing
        for i in range(4):
            master.grid_columnconfigure(i, weight=1)
        for i in range(1, 6):
            master.grid_rowconfigure(i, weight=1)

        # Define button layout (text, row, col, style)
        buttons = [
            ('AC', 1, 0, 'special'), ('/', 1, 3, 'operator'),
            ('7', 2, 0, 'number'), ('8', 2, 1, 'number'), ('9', 2, 2, 'number'), ('*', 2, 3, 'operator'),
            ('4', 3, 0, 'number'), ('5', 3, 1, 'number'), ('6', 3, 2, 'number'), ('-', 3, 3, 'operator'),
            ('1', 4, 0, 'number'), ('2', 4, 1, 'number'), ('3', 4, 2, 'number'), ('+', 4, 3, 'operator'),
            ('0', 5, 0, 'number_wide'), ('.', 5, 2, 'number'), ('=', 5, 3, 'equal')
        ]
        
        # Create and place buttons
        for (text, row, col, style) in buttons:
            self.create_button(text, row, col, style)

    def button_click(self, text):
        """Handles all button click events."""
        
        if text == 'AC':
            result = self.calc.clear_expression()
        elif text == '=':
            result = self.calc.calculate()
        else:
            result = self.calc.append_to_expression(text)
            
        self.display_text.set(result)

    def create_button(self, text, row, col, style):
        """Creates a button with specific styling and placement."""
        
        # Define base styles
        base_style = {'font': ('Arial', 14), 'height': 2, 'bd': 0, 'relief': 'flat'}
        
        # Apply style variations
        if style == 'number':
            style_vars = {'bg': "#505050", 'fg': "white"}
        elif style == 'number_wide':
            style_vars = {'bg': "#505050", 'fg': "white", 'columnspan': 2}
        elif style == 'operator':
            style_vars = {'bg': "#ff9500", 'fg': "white"}
        elif style == 'special': # AC
            style_vars = {'bg': "#d4d4d2", 'fg': "black"}
        elif style == 'equal':
            style_vars = {'bg': "#5cb85c", 'fg': "white"}
        
        btn = tk.Button(self.master, text=text, **base_style, **style_vars,
                        command=lambda t=text: self.button_click(t))
        
        # Determine spanning based on the wide number
        span = 2 if style == 'number_wide' else 1
        
        # Place the button on the grid
        btn.grid(row=row, column=col, columnspan=span, sticky="nsew", padx=5, pady=5)
        
        # Add a subtle hover effect (optional, but professional)
        def on_enter(e):
            e.widget['bg'] = '#777777' if style == 'number' else e.widget['bg']
        def on_leave(e):
            e.widget['bg'] = '#505050' if style == 'number' else style_vars['bg']
            
        btn.bind("<Enter>", on_enter)
        btn.bind("<Leave>", on_leave)


if __name__ == '__main__':
    root = tk.Tk()
    app = CalculatorApp(root)
    root.mainloop()
