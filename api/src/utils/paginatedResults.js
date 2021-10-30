import { Op } from 'sequelize';
import { Sequelize } from '../models';
export const paginatedResults = async (model, data, excludeFields=[], searchFields = [], inclusions = []) =>{

    const {
        sortBy = 'id',
        sortDesc = 'asc',
        page = 1,
        itemsPerPage = 10,
        extraFields = []
    } = data;

    let sortByAssociation = null;
    if (sortBy.indexOf('.') !== -1) {
        const sortValues = sortBy.split('.');
        const modelToSort = inclusions.find( item => item.as == sortValues[0] );
        if (modelToSort) {
            sortByAssociation = [[modelToSort, sortValues[1], sortDesc]];
        }
    }

    let sqlParams = {
        limit: +itemsPerPage,
        order: sortByAssociation || [[sortBy, sortDesc]],
        offset: ((page -1 ) * itemsPerPage),
        attributes: {
            exclude: excludeFields
        },
        include: inclusions,
        where:{}
    }

    if(extraFields.length > 0){
        extraFields.forEach(item=>{
            sqlParams.where = [
                sqlParams.where,
                {
                    [item[0]]:{
                        [item[2]]: item[1]
                    }
                }
            ]
        })
    }

    const addPercent = (string) =>{
        return `'%${string}%'`
    }
    if(searchFields.length > 0){
        if(data.search && data.search.length >= 3 && data.search != ''){
            sqlParams.where = [
                sqlParams.where, 
                { [Op.or]: searchFields.map( item => Sequelize.literal(item + ' like ' + addPercent(data.search) ) ) }
            ];
        }
    }

    const result = await model.findAndCountAll(sqlParams)

    return result;
}