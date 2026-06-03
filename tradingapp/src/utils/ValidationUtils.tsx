export const validateEmail = (email: string) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

export function validatePasswordLength(password) {
  // 1) check length
  if (password.length < 8 || password.length > 20) {
    return false;
  }

  // 2) check for at least one letter and one digit
  let hasLetter = false;
  let hasDigit = false;

  for (let ch of password) {
    if (ch >= '0' && ch <= '9') {
      hasDigit = true;
    } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
      hasLetter = true;
    }
    // once both are true, we can stop
    if (hasLetter && hasDigit) {
      return true;
    }
  }

  // if we finish the loop without both flags, it’s invalid
  return false;
}


export const validatePasswordEntry = (
  password: string,
  name: string,
  email: string,
) => {
  if (!validatePasswordLength(password)) {
    return {
      msg: 'Password length must be 8 to 20 characters',
      result: false,
    };
  }

  if (name && password.toLowerCase().includes(name.toLowerCase())) {
    return {
      msg: "Must not contain user's name",
      result: false,
    };
  }

  if (email && password.toLowerCase().includes(email.toLowerCase())) {
    return {
      msg: "Must not contain user's email id",
      result: false,
    };
  }

  return {
    msg: 'Passed Local Test!',
    result: true,
  };
};