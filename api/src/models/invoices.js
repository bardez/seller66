export default (sequelize, DataTypes) => {

    const InvoicesModel = sequelize.define("InvoicesModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        client_id: {
            type: DataTypes.INTEGER(),
            references:{
                model: 'Clients',
                key: 'id'
            }
        },
        date: DataTypes.DATE(),
        status: DataTypes.ENUM('A', 'F')
    },{
        tableName: 'invoices',
        timestamps: false,
        paranoid: false
    });

    return InvoicesModel;
}