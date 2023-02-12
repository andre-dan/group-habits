class ItemPresenter < BasePresenter
  attr_accessor :item

  def initialize(item)
    @item = item

    super()
  end

  def payload
    {
      id: item.id,
      name: item.name,
      extra_info: item.extra_info,
      sale_price: item.sale_price.to_f,
      purchase_price: item.purchase_price.to_f,
      item_type: item.item_type,
      category: item.category.nil? ? nil : CategoryPresenter.payload_for(item.category),
      sales_unit: item.sales_unit.nil? ? nil : SalesUnitPresenter.payload_for(item.sales_unit)
    }
  end
end
