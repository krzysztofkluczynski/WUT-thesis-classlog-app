import { ErrorResponse } from './error-response';

describe('ErrorResponse', () => {
  it('should create an instance with provided values', () => {
    const error = new ErrorResponse(404, 'Not Found', { resource: 'Task' });

    expect(error.getStatusCode()).toBe(404);
    expect(error.getMessage()).toBe('Not Found');
    expect(error.getDetails()).toEqual({ resource: 'Task' });
  });

  it('should allow setting and getting the status code', () => {
    const error = new ErrorResponse(500, 'Server Error');

    error.setStatusCode(400);
    expect(error.getStatusCode()).toBe(400);
  });

  it('should allow setting and getting the message', () => {
    const error = new ErrorResponse(500, 'Server Error');

    error.setMessage('Bad Request');
    expect(error.getMessage()).toBe('Bad Request');
  });

  it('should allow setting and getting the details', () => {
    const error = new ErrorResponse(500, 'Server Error');

    const details = { field: 'name', issue: 'missing' };
    error.setDetails(details);
    expect(error.getDetails()).toEqual(details);
  });

  it('should handle undefined details gracefully', () => {
    const error = new ErrorResponse(500, 'Server Error');

    expect(error.getDetails()).toBeUndefined();
  });

  it('should update all values correctly using setters', () => {
    const error = new ErrorResponse(500, 'Server Error', { info: 'Original' });

    error.setStatusCode(200);
    error.setMessage('OK');
    error.setDetails({ info: 'Updated' });

    expect(error.getStatusCode()).toBe(200);
    expect(error.getMessage()).toBe('OK');
    expect(error.getDetails()).toEqual({ info: 'Updated' });
  });
});
