export default (sequelize, DataTypes) => {
    const SellerRoutesModel = sequelize.define("SellerRoutesModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        route_id: DataTypes.INTEGER(),
        seller_id: DataTypes.INTEGER(),
    },{
        tableName: 'seller_routes',
        timestamps: false,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
        
    });
    
    return SellerRoutesModel;
}