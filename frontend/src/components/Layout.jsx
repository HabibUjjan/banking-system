import React, { useState } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import {
    AppBar, Box, Drawer, IconButton, List, ListItem, ListItemButton,
    ListItemIcon, ListItemText, Toolbar, Typography, Avatar, CssBaseline,
} from '@mui/material';
import {
    Menu as MenuIcon, Dashboard, AccountBalance, SwapHoriz,
    ReceiptLong, People, Person, Logout,
} from '@mui/icons-material';
import { useAuth } from '../AuthContext';

const drawerWidth = 260;

const menuItems = [
    { text: 'Dashboard', icon: <Dashboard />, path: '/dashboard' },
    { text: 'Accounts', icon: <AccountBalance />, path: '/accounts' },
    { text: 'Transfer', icon: <SwapHoriz />, path: '/transfer' },
    { text: 'Transactions', icon: <ReceiptLong />, path: '/transactions' },
    { text: 'Beneficiaries', icon: <People />, path: '/beneficiaries' },
    { text: 'Profile', icon: <Person />, path: '/profile' },
];

const Layout = () => {
    const [mobileOpen, setMobileOpen] = useState(false);
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const drawer = (
        <Box>
            <Box sx={{ p: 3, textAlign: 'center', bgcolor: 'primary.main', color: 'white' }}>
                <Avatar sx={{ width: 60, height: 60, mx: 'auto', mb: 1, bgcolor: 'white', color: 'primary.main', fontSize: 24, fontWeight: 'bold' }}>
                    {user?.firstName?.[0]}{user?.lastName?.[0]}
                </Avatar>
                <Typography variant="h6">{user?.firstName} {user?.lastName}</Typography>
                <Typography variant="body2" sx={{ opacity: 0.9 }}>{user?.email}</Typography>
            </Box>
            <List sx={{ p: 2 }}>
                {menuItems.map((item) => (
                    <ListItem key={item.text} disablePadding sx={{ mb: 0.5 }}>
                        <ListItemButton
                            onClick={() => { navigate(item.path); setMobileOpen(false); }}
                            selected={location.pathname === item.path}
                            sx={{
                                borderRadius: 2,
                                '&.Mui-selected': {
                                    bgcolor: 'primary.main',
                                    color: 'white',
                                    '&:hover': { bgcolor: 'primary.dark' },
                                    '& .MuiListItemIcon-root': { color: 'white' }
                                },
                            }}
                        >
                            <ListItemIcon sx={{ minWidth: 40 }}>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.text} />
                        </ListItemButton>
                    </ListItem>
                ))}
                <ListItem disablePadding sx={{ mt: 2 }}>
                    <ListItemButton onClick={handleLogout} sx={{ borderRadius: 2, color: 'error.main' }}>
                        <ListItemIcon sx={{ minWidth: 40 }}><Logout color="error" /></ListItemIcon>
                        <ListItemText primary="Logout" />
                    </ListItemButton>
                </ListItem>
            </List>
        </Box>
    );

    return (
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <AppBar
                position="fixed"
                sx={{
                    width: { sm: `calc(100% - ${drawerWidth}px)` },
                    ml: { sm: `${drawerWidth}px` },
                    bgcolor: 'white',
                    color: 'text.primary',
                    boxShadow: 1
                }}
            >
                <Toolbar>
                    <IconButton edge="start" onClick={() => setMobileOpen(!mobileOpen)} sx={{ mr: 2, display: { sm: 'none' } }}>
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 'bold', color: 'primary.main' }}>
                        🏦 Digital Banking
                    </Typography>
                </Toolbar>
            </AppBar>

            <Box component="nav" sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}>
                <Drawer
                    variant="temporary"
                    open={mobileOpen}
                    onClose={() => setMobileOpen(false)}
                    sx={{ display: { xs: 'block', sm: 'none' }, '& .MuiDrawer-paper': { width: drawerWidth } }}
                >
                    {drawer}
                </Drawer>
                <Drawer
                    variant="permanent"
                    sx={{ display: { xs: 'none', sm: 'block' }, '& .MuiDrawer-paper': { width: drawerWidth } }}
                    open
                >
                    {drawer}
                </Drawer>
            </Box>

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                    width: { sm: `calc(100% - ${drawerWidth}px)` },
                    mt: 8,
                    bgcolor: '#f5f5f5',
                    minHeight: '100vh'
                }}
            >
                <Outlet />
            </Box>
        </Box>
    );
};

export default Layout;