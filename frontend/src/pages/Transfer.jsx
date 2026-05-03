import React from 'react';
import { Box, Typography, Paper } from '@mui/material';

const Transfer = () => (
    <Box>
        <Typography variant="h4" fontWeight="bold" gutterBottom>Transfer Money</Typography>
        <Paper sx={{ p: 3 }}>
            <Typography color="text.secondary">
                Money transfer features will be available here.
            </Typography>
        </Paper>
    </Box>
);

export default Transfer;