import React from 'react';
import { Box, Typography, Paper, Button } from '@mui/material';
import { Add } from '@mui/icons-material';

const Accounts = () => (
    <Box>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
            <Typography variant="h4" fontWeight="bold">My Accounts</Typography>
            <Button variant="contained" startIcon={<Add />}>New Account</Button>
        </Box>
        <Paper sx={{ p: 3 }}>
            <Typography color="text.secondary">
                Account management features will be available here.
            </Typography>
        </Paper>
    </Box>
);

export default Accounts;