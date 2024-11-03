export function parseDate(dateInput: any): Date {
  if (Array.isArray(dateInput)) {
    // The array format received from the backend
    const [year, month, day, hour, minute, second, millisecond] = dateInput;
    return new Date(Date.UTC(year, month - 1, day, hour, minute, second, millisecond / 1000));
  } else if (typeof dateInput === 'string') {
    // If it's a string, just parse it as a Date
    return new Date(dateInput);
  }
  // If the input is not an expected format, return the current date as a fallback
  return new Date();
}
