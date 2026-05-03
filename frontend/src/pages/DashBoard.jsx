import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Box, Grid, Paper, Typography, Card, CardContent, Button,
    CircularProgress, Chip,
} from '@mui/material';
import { AccountBalance, SwapHoriz, Receipt, Add } from '@mui/icons-material';
import { api, useAuth } from '../AuthContext';
import { toast } from 'react-toastify';

const Dashboard = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [dashboardData, setDashboardData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchDashboard();
    }, []);

    const fetchDashboard = async () => {
        try {
            const response = await api.get('/dashboard');
            setDashboardData(response.data.data);
        } catch (error) {
            toast.error('Failed to load dashboard');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
                <CircularProgress size={50} />
            </Box>
        );
    }

    const accounts = dashboardData?.accounts || [];
    const totalBalance = dashboardData?.totalBalance || 0;

    return (
        <Box className="fade-in">
            <Box sx={{ mb: 4 }}>
                <Typography variant="h4" fontWeight="bold" gutterBottom>
                    Welcome, {user?.firstName}! 👋
                </Typography>
                <Typography color="text.secondary">
                    Here's your financial overview
                </Typography>
            </Box>

            {/* Total Balance Card */}
            <Paper sx={{ p: 4, mb: 4, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white' }}>
                <Typography variant="subtitle1" sx={{ opacity: 0.9 }}>Total Balance</Typography>
                <Typography variant="h3" sx={{ my: 2, fontWeight: 'bold' }}>
                    ${Number(totalBalance).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                </Typography>
                <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
                    <Button variant="contained" startIcon={<SwapHoriz />} onClick={() => navigate('/transfer')}
                            sx={{ bgcolor: 'white', color: '#764ba2', '&:hover': { bgcolor: '#f5f5f5' } }}>
                        Transfer Money
                    </Button>
                    <Button variant="outlined" startIcon={<Add />} onClick={() => navigate('/accounts')}
                            sx={{ color: 'white', borderColor: 'white', '&:hover': { borderColor: '#f5f5f5' } }}>
                        New Account
                    </Button>
                </Box>
            </Paper>

            {/* Quick Actions */}
            <Grid container spacing={2} sx={{ mb: 4 }}>
                {[
                    { title: 'Transfer', icon: <SwapHoriz sx={{ fontSize: 40 }} />, path: '/transfer', color: '#667eea' },
                    { title: 'Accounts', icon: <AccountBalance sx={{ fontSize: 40 }} />, path: '/accounts', color: '#2e7d32' },
                    { title: 'History', icon: <Receipt sx={{ fontSize: 40 }} />, path: '/transactions', color: '#ed6c02' },
                ].map((action) => (
                    <Grid item xs={6} sm={4} key={action.title}>
                        <Card onClick={() => navigate(action.path)} sx={{ cursor: 'pointer', textAlign: 'center', py: 2, transition: '0.2s', '&:hover': { transform: 'translateY(-4px)' } }}>
                            <Box sx={{ color: action.color, mb: 1 }}>{action.icon}</Box>
                            <Typography fontWeight="bold">{action.title}</Typography>
                        </Card>
                    </Grid>
                ))}
            </Grid>

            {/* Accounts */}
            <Typography variant="h5" fontWeight="bold" gutterBottom sx={{ mb: 2 }}>
                Your Accounts
            </Typography>
            <Grid container spacing={3}>
                {accounts.length === 0 ? (
                    <Grid item xs={12}>
                        <Paper sx={{ p: 4, textAlign: 'center' }}>
                            <Typography color="text.secondary">No accounts yet. Create one to get started!</Typography>
                            <Button variant="contained" sx={{ mt: 2 }} onClick={() => navigate('/accounts')}>Create Account</Button>
                        </Paper>
                    </Grid>
                ) : (
                    accounts.map((account) => (
                        <Grid item xs={12} md={6} key={account.id}>
                            <Paper sx={{ p: 3 }}>
                                <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                                    <Typography variant="h6">{account.accountType} Account</Typography>
                                    <Chip label={account.status} color="success" size="small" />
                                </Box>
                                <Typography variant="body2" color="text.secondary">{account.accountNumber}</Typography>
                                <Typography variant="h5" fontWeight="bold" color="primary" sx={{ mt: 1 }}>
                                    ${Number(account.balance).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                </Typography>
                            </Paper>
                        </Grid>
                    ))
                )}
            </Grid>
        </Box>
    );
};

export default Dashboard;