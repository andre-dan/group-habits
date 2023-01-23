module Api
  module V1
    class ItemsController < Api::V1::ApiController
      after_action :verify_policy_scoped, only: []
      after_action :verify_authorized, only: []

      def index
        items = ItemsFinderService.new(user: current_user, index_params:).call
        render json: ItemPresenter.payload_for_list(items)
      end

      private

      def index_params
        params.permit(:name)
      end
    end
  end
end
