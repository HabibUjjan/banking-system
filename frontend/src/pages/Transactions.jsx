import React from 'react';
import { Box, Typography, Paper } from '@mui/material';

const Transactions = () => (
    <Box>
        <Typography variant="h4" fontWeight="bold" gutterBottom>Transaction History</Typography>
        <Paper sx={{ p: 3 }}>
            <Typography color="text.secondary">
                Transaction history will appear here.
            </Typography>
        </Paper>
    </Box>
);

export default Transactions;