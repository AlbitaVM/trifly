package es.albavm.tfg.trifly.dto.User;

import java.util.List;

public class AdminStatsDto {
    private long totalUsers;
    private long totalItineraries;
    private long totalBudgets;
    private long newUsersThisMonth;
    private long activeUsers;
    private long inactiveUsers;
    private double avgItinerariesPerUser;
    private double growthThisMonth;
    private List<Integer> usersPerMonth; 
    private List<Integer> itinerariesPerMonth; 

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalItineraries() {
        return totalItineraries;
    }

    public long getTotalBudgets() {
        return totalBudgets;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public double getAvgItinerariesPerUser() {
        return avgItinerariesPerUser;
    }

    public double getGrowthThisMonth() {
        return growthThisMonth;
    }

    public long getInactiveUsers() {
        return inactiveUsers;
    }

    public long getNewUsersThisMonth() {
        return newUsersThisMonth;
    }

    public List<Integer> getItinerariesPerMonth() {
        return itinerariesPerMonth;
    }

    public List<Integer> getUsersPerMonth() {
        return usersPerMonth;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setTotalItineraries(long totalItineraries) {
        this.totalItineraries = totalItineraries;
    }

    public void setTotalBudgets(long totalBudgets) {
        this.totalBudgets = totalBudgets;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public void setAvgItinerariesPerUser(double avgItinerariesPerUser) {
        this.avgItinerariesPerUser = avgItinerariesPerUser;
    }

    public void setGrowthThisMonth(double growthThisMonth) {
        this.growthThisMonth = growthThisMonth;
    }
    
    public void setInactiveUsers(long inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    public void setNewUsersThisMonth(long newUsersThisMonth) {
        this.newUsersThisMonth = newUsersThisMonth;
    }

    public void setItinerariesPerMonth(List<Integer> itinerariesPerMonth) {
        this.itinerariesPerMonth = itinerariesPerMonth;
    }

    public void setUsersPerMonth(List<Integer> usersPerMonth) {
        this.usersPerMonth = usersPerMonth;
    }
}
