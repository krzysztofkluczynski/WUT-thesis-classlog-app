export function parseDate(dateInput: any): Date {
  if (Array.isArray(dateInput)) {
    // Handle array format received from the backend
    const [year = 1970, month = 1, day = 1, hour = 0, minute = 0, second = 0, millisecond = 0] = dateInput;

    // Ensure month is adjusted for 0-based indexing in JavaScript Dates
    return new Date(Date.UTC(year, month - 1, day, hour, minute, second, millisecond / 1000));
  } else if (typeof dateInput === 'string') {
    // If it's a string, parse it as a Date
    return new Date(dateInput);
  }

  // If the input is not an expected format, return the current date as a fallback
  return new Date();
}
