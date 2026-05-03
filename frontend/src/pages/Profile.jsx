import React from 'react';
import { Box, Typography, Paper, Avatar } from '@mui/material';
import { useAuth } from '../AuthContext';

const Profile = () => {
    const { user } = useAuth();

    return (
        <Box>
            <Typography variant="h4" fontWeight="bold" gutterBottom>My Profile</Typography>
            <Paper sx={{ p: 4, maxWidth: 500, textAlign: 'center' }}>
                <Avatar sx={{ width: 100, height: 100, mx: 'auto', mb: 2, fontSize: 40, bgcolor: 'primary.main' }}>
                    {user?.firstName?.[0]}{user?.lastName?.[0]}
                </Avatar>
                <Typography variant="h5">{user?.firstName} {user?.lastName}</Typography>
                <Typography color="text.secondary">@{user?.username}</Typography>
                <Typography sx={{ mt: 1 }}>{user?.email}</Typography>
            </Paper>
        </Box>
    );
};

export default Profile;