import React from 'react';
import { Box, Typography, Paper } from '@mui/material';

const Beneficiaries = () => (
    <Box>
        <Typography variant="h4" fontWeight="bold" gutterBottom>Beneficiaries</Typography>
        <Paper sx={{ p: 3 }}>
            <Typography color="text.secondary">
                Beneficiary management will be available here.
            </Typography>
        </Paper>
    </Box>
);

export default Beneficiaries;