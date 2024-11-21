export function parseDate(dateInput: any): Date {
  if (typeof dateInput === 'string') {
    // Handle ISO 8601 string with microseconds
    const [datePart, timePart] = dateInput.split('T');
    if (timePart && timePart.includes('.')) {
      const [time, microseconds] = timePart.split('.');
      const formattedDateTime = `${datePart}T${time}.${microseconds.slice(0, 3)}Z`; // Truncate to milliseconds and add 'Z' for UTC
      return new Date(formattedDateTime);
    }
    return new Date(dateInput); // If no microseconds, parse directly
  }

  if (Array.isArray(dateInput)) {
    // Convert nanoseconds (6th element) to milliseconds
    const [year = 1970, month = 1, day = 1, hour = 0, minute = 0, second = 0, nanoseconds = 0] = dateInput;
    const milliseconds = Math.floor(nanoseconds / 1_000_000); // Convert nanoseconds to milliseconds
    return new Date(year, month - 1, day, hour, minute, second, milliseconds);
  }

  // Fallback to the current date
  return new Date();
}
