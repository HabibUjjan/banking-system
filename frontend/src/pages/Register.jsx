import React, { useState } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
    Box, Container, Paper, Typography, TextField, Button, Link,
    Alert, CircularProgress, Grid,
} from '@mui/material';
import axios from 'axios';
import { toast } from 'react-toastify';

const API_URL = 'http://localhost:8080/api/v1';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        phone: '',
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Clear previous error
        setError('');

        // Validate passwords match
        if (formData.password !== formData.confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        // Validate password length
        if (formData.password.length < 6) {
            setError('Password must be at least 6 characters');
            return;
        }

        setLoading(true);
        console.log('Attempting registration with:', {
            username: formData.username,
            email: formData.email,
            firstName: formData.firstName,
            lastName: formData.lastName,
        });

        try {
            // Remove confirmPassword before sending
            const { confirmPassword, ...registerData } = formData;

            const response = await axios.post(`${API_URL}/auth/register`, registerData);
            console.log('Registration response:', response.data);

            toast.success('Registration successful! Please login.');

            // Redirect to login after short delay
            setTimeout(() => {
                navigate('/login');
            }, 1000);

        } catch (err) {
            console.error('Registration error:', err);
            console.error('Error response:', err.response);

            // Get error message from backend
            let errorMessage = 'Registration failed';

            if (err.response) {
                // Server responded with error
                if (err.response.data) {
                    errorMessage = err.response.data.message ||
                        err.response.data.error ||
                        JSON.stringify(err.response.data);
                }
                console.log('Server error status:', err.response.status);
                console.log('Server error data:', err.response.data);
            } else if (err.request) {
                // Request was made but no response
                errorMessage = 'Cannot connect to server. Make sure backend is running on port 8080.';
            } else {
                // Something else
                errorMessage = err.message;
            }

            setError(errorMessage);
            toast.error(errorMessage);

        } finally {
            setLoading(false);
        }
    };

    const fields = [
        { name: 'firstName', label: 'First Name', sm: 6, required: true },
        { name: 'lastName', label: 'Last Name', sm: 6, required: true },
        { name: 'username', label: 'Username', sm: 6, required: true },
        { name: 'email', label: 'Email', sm: 6, required: true, type: 'email' },
        { name: 'password', label: 'Password', sm: 6, required: true, type: 'password' },
        { name: 'confirmPassword', label: 'Confirm Password', sm: 6, required: true, type: 'password' },
        { name: 'phone', label: 'Phone Number', sm: 12, required: false },
    ];

    return (
        <Box sx={{
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'center',
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            py: 4
        }}>
            <Container maxWidth="md">
                <Paper elevation={10} sx={{ p: 5, borderRadius: 4 }}>
                    <Typography variant="h4" fontWeight="bold" textAlign="center" gutterBottom>
                        Create Account
                    </Typography>
                    <Typography variant="body1" color="text.secondary" textAlign="center" mb={3}>
                        Join Digital Banking today
                    </Typography>

                    {error && (
                        <Alert severity="error" sx={{ mb: 2 }}>
                            {error}
                        </Alert>
                    )}

                    <form onSubmit={handleSubmit}>
                        <Grid container spacing={2}>
                            {fields.map((field) => (
                                <Grid item xs={12} sm={field.sm} key={field.name}>
                                    <TextField
                                        fullWidth
                                        label={field.label}
                                        name={field.name}
                                        type={field.type || 'text'}
                                        value={formData[field.name]}
                                        onChange={handleChange}
                                        required={field.required}
                                        disabled={loading}
                                    />
                                </Grid>
                            ))}
                        </Grid>

                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            size="large"
                            disabled={loading}
                            sx={{ mt: 3, py: 1.5 }}
                        >
                            {loading ? <CircularProgress size={24} color="inherit" /> : 'Create Account'}
                        </Button>

                        <Box textAlign="center" mt={2}>
                            <Typography variant="body2">
                                Already have an account?{' '}
                                <Link component={RouterLink} to="/login" fontWeight="bold">
                                    Sign in
                                </Link>
                            </Typography>
                        </Box>
                    </form>
                </Paper>
            </Container>
        </Box>
    );
};

export default Register;