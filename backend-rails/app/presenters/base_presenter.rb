class BasePresenter
  attr_accessor :category

  def self.payload_for(resource)
    if resource.is_a?(Array) || resource.is_a?(ActiveRecord::Relation)
      payload_for_resources(resource)
    else
      payload_for_resource(resource)
    end
  end

  def self.payload_for_resource(item)
    new(item).payload
  end

  def self.payload_for_resources(items)
    items.map do |item|
      payload_for_resource(item)
    end
  end

  private_class_method :payload_for_resource, :payload_for_resources
end
